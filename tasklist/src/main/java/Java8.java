import java.util.stream.Stream;

/**
 * Created by gosha on 10/20/16.
 */
public class Java8 {
  public static void main(String[] args) {
    Stream.of(args).forEach(System.out::println);
  }
}
