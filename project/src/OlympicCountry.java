import java.util.*;

public class OlympicCountry extends Country {
    // 추가 속성
    private List<String> events;                     // 참가 종목 리스트
    private Map<String, Map<String, Integer>> medalsByEvent; // 종목별 메달 기록
    public Map<String, Integer> getMedalsByEventMap(String event) {
        return medalsByEvent.getOrDefault(event, new HashMap<>());
    }

    // 생성자
    public OlympicCountry(String name, String continent, List<String> events) {
        super(name, continent); // 부모 클래스의 생성자 호출
        this.events = events; // 참가 종목 초기화
        this.medalsByEvent = new HashMap<>(); // 종목별 메달 정보 초기화
        for (String event : events) {
            medalsByEvent.put(event, new HashMap<>()); // 각 종목별 메달 기록 초기화
        }
    }

    // 참가 종목 반환
    public List<String> getEvents() {
        return events;
    }

    // 종목별 메달 기록 추가
    public void addMedalByEvent(String event, String medalType) throws InvalidEventException {
        // 참가하지 않은 종목일 경우 예외 발생
        if (!events.contains(event)) {
            throw new InvalidEventException(event + "는 참가하지 않은 종목입니다.");
        }

        // 종목에 해당 메달 타입 추가
        Map<String, Integer> eventMedals = medalsByEvent.get(event);
        eventMedals.put(medalType, eventMedals.getOrDefault(medalType, 0) + 1);

        // 부모 클래스의 총 메달 수 갱신
        super.addMedal(medalType);
    }

    // 특정 종목에서 획득한 메달 수 반환
    public int getMedalsByEvent(String event, String medalType) {
        if (!medalsByEvent.containsKey(event)) {
            return 0; // 해당 종목의 메달 데이터가 없으면 0 반환
        }
        return medalsByEvent.get(event).getOrDefault(medalType, 0);
    }

    // 특정 종목의 총 메달 수 반환
    public int getTotalMedalsByEvent(String event) {
        if (!medalsByEvent.containsKey(event)) {
            return 0;
        }
        return medalsByEvent.get(event).values().stream().mapToInt(Integer::intValue).sum();
    }

    public void removeMedalByEvent(String event, String medalType) throws InvalidEventException {
        // 종목이 유효한지 확인
        if (!events.contains(event)) {
            throw new InvalidEventException(event + "는 참가하지 않은 종목입니다.");
        }

        // 메달 타입이 유효한지 확인
        if (!medalType.equals("gold") && !medalType.equals("silver") && !medalType.equals("bronze")) {
            throw new IllegalArgumentException("잘못된 메달 타입: " + medalType);
        }

        // 종목에 대한 메달 기록 확인
        Map<String, Integer> eventMedals = medalsByEvent.get(event);
        if (eventMedals.getOrDefault(medalType, 0) > 0) {
            eventMedals.put(medalType, eventMedals.get(medalType) - 1);
            super.removeMedal(medalType); // 부모 클래스에서 총 메달 수 감소
        } else {
            throw new InvalidEventException("삭제할 " + medalType + " 메달이 없습니다.");
        }
    }

    // toString() 메서드 오버라이드: 국가 정보와 종목별 메달 기록 출력
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("\n"); // 부모 클래스의 국가 정보 출력
        sb.append("참가 종목 및 메달 기록:\n");
        for (String event : events) {
            sb.append("- ").append(event).append(": ");
            Map<String, Integer> eventMedals = medalsByEvent.get(event);
            sb.append("Gold: ").append(eventMedals.getOrDefault("gold", 0)).append(", ");
            sb.append("Silver: ").append(eventMedals.getOrDefault("silver", 0)).append(", ");
            sb.append("Bronze: ").append(eventMedals.getOrDefault("bronze", 0)).append("\n");
        }
        return sb.toString();
    }
}