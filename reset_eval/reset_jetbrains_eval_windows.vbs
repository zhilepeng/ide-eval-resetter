' reset jetbrains ide evals v1.0.4
Set oShell = CreateObject("WScript.Shell")
Set oFS = CreateObject("Scripting.FileSystemObject")
sHomeFolder = oShell.ExpandEnvironmentStrings("%USERPROFILE%")
sJBDataFolder = oShell.ExpandEnvironmentStrings("%APPDATA%") + "\JetBrains"

Set re = New RegExp
re.Global     = True
re.IgnoreCase = True
re.Pattern    = "\.?(IntelliJIdea|GoLand|CLion|PyCharm|DataGrip|RubyMine|AppCode|PhpStorm|WebStorm|Rider).*"

Sub removeEval(ByVal file, ByVal sEvalPath)
    bMatch = re.Test(file.Name)
    If Not bMatch Then
        Exit Sub
    End If

    If oFS.FolderExists(sEvalPath) Then
        oFS.DeleteFolder sEvalPath, True
    End If

    content = ""
    otherFile = oFS.GetParentFolderName(sEvalPath) + "\options\other.xml"
    If oFS.FileExists(otherFile) Then
        Set txtStream = oFS.OpenTextFile(otherFile, 1, False)
        Do While Not txtStream.AtEndOfStream
            line = txtStream.ReadLine
            If InStr(line, "name=""evlsprt") = 0 Then
                content = content + line + vbLf
            End If
        Loop
        txtStream.Close

        Set txtStream = oFS.OpenTextFile(otherFile, 2, False)
        txtStream.Write content
        txtStream.Close
    End If
End Sub

If oFS.FolderExists(sHomeFolder) Then
    For Each oFile In oFS.GetFolder(sHomeFolder).SubFolders
        removeEval oFile, sHomeFolder + "\" + oFile.Name + "\config\eval"
    Next
End If

If oFS.FolderExists(sJBDataFolder) Then
    For Each oFile In oFS.GetFolder(sJBDataFolder).SubFolders
        removeEval oFile, sJBDataFolder + "\" + oFile.Name + "\eval"
    Next
End If

On Error Resume Next
oShell.RegDelete "HKEY_CURRENT_USER\Software\JavaSoft\Prefs\/Jet/Brains./User/Id/On/Machine"
oShell.RegDelete "HKEY_CURRENT_USER\Software\JavaSoft\Prefs\jetbrains\device_id"
oShell.RegDelete "HKEY_CURRENT_USER\Software\JavaSoft\Prefs\jetbrains\user_id_on_machine"
oFs.DeleteFile sJBDataFolder + "\bl"
oFs.DeleteFile sJBDataFolder + "\crl"
oFs.DeleteFile sJBDataFolder + "\PermanentUserId"
oFs.DeleteFile sJBDataFolder + "\PermanentDeviceId"

MsgBox "done"