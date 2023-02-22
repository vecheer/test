package other.scanner;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author yq
 * @version v1.0 2023-02-06 9:28 PM
 */
public class TestMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> inputList = new LinkedList<>();
        label:
        while (true) {
            System.out.print(">");
            String input = scanner.next();
            switch (input) {
                case ":list":
                    for (String s : inputList) {
                        System.out.print(s);
                    }
                    System.out.println();
                    break;
                case ":undo":
                    if (inputList.size() <= 0)
                        System.out.println("当前没有足够多的元素，请尝试输入一些新元素! 请继续输入！");
                    else
                        inputList.remove(inputList.size() - 1);
                    break;
                case ":over":
                    inputList.clear();
                    break label;
                default:
                    inputList.add(input);
            }
        }

        System.out.println("bye...");

        scanner.close();
    }

}
