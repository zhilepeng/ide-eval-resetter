import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tsss {
    List<MyInterface> list = Arrays.asList();

    public static void main(String[] args) {
       new Tsss().add(new MyInterface() {
           @Override
           public void test() {

           }
       });
    }

    public void add(MyInterface obj) {
        list.add(obj);
    }
}
