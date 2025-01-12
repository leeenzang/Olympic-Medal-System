// 참가하지 않은 국가를 입력했을 때 에외처리
public class InvalidCountryException extends Exception {
    public InvalidCountryException(String message) {
        super(message);
    }
}