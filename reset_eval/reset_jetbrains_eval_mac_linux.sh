#!/bin/bash
# reset jetbrains ide evals

OS_NAME=$(uname -s)
JB_PRODUCTS="IntelliJIdea CLion PhpStorm GoLand PyCharm WebStorm Rider DataGrip RubyMine AppCode"

if [ "$OS_NAME" == "Darwin" ]; then
  echo 'macOS:'

  for PRD in $JB_PRODUCTS; do
    rm -rf ~/Library/Preferences/"${PRD}"*/eval
    rm -rf ~/Library/Application\ Support/JetBrains/"${PRD}"*/eval
  done

  plutil -remove "/.JetBrains\.UserIdOnMachine" ~/Library/Preferences/com.apple.java.util.prefs.plist >/dev/null
  plutil -remove "/.jetbrains/.user_id_on_machine" ~/Library/Preferences/com.apple.java.util.prefs.plist >/dev/null
  plutil -remove "/.jetbrains/.device_id" ~/Library/Preferences/com.apple.java.util.prefs.plist >/dev/null
elif [ "$OS_NAME" == "Linux" ]; then
  echo 'Linux:'

  for PRD in $JB_PRODUCTS; do
    rm -rf ~/."${PRD}"*/config/eval
    rm -rf ~/.config/JetBrains/"${PRD}"*/eval
  done

  sed -i '/key="JetBrains\.UserIdOnMachine"/d' ~/.java/.userPrefs/prefs.xml
  sed -i '/key="device_id"/d' ~/.java/.userPrefs/jetbrains/prefs.xml
  sed -i '/key="user_id_on_machine"/d' ~/.java/.userPrefs/jetbrains/prefs.xml
else
  echo 'unsupport'
  exit
fi

echo 'done.'
