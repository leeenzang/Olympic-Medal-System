// 참가하지 않은 종목을 입력했을 때 예외처리
public class InvalidEventException extends Exception {
    public InvalidEventException(String message) {
        super(message);
    }
}