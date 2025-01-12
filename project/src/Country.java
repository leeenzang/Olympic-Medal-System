public class Country {
    // 국가 정보를 저장하는 클래스

    // 1. 기본 속성
    private String name;        // 국가 이름
    private String continent;   // 대륙
    private int totalGold;      // 금메달 수
    private int totalSilver;    // 은메달 수
    private int totalBronze;    // 동메달 수

    // 2. 생성자: 국가 이름과 대륙을 설정하고 메달 개수는 0으로 초기화
    public Country(String name, String continent) {
        this.name = name;
        this.continent = continent;
        this.totalGold = 0;
        this.totalSilver = 0;
        this.totalBronze = 0;
    }

    // 3. 메서드

    // (1) 메달 추가 메서드
    // 메달 타입에 따라 금, 은, 동 메달 개수를 1 증가시킴
    public void addMedal(String medalType) {
        switch (medalType.toLowerCase()) {
            case "gold" -> totalGold++;
            case "silver" -> totalSilver++;
            case "bronze" -> totalBronze++;
            default -> System.out.println("잘못된 메달 타입입니다: " + medalType);
        }
    }

    // (2) 메달 삭제 메서드
    // 메달 개수를 감소시키되, 0 이하로는 내려가지 않음
    public void removeMedal(String medalType) {
        switch (medalType.toLowerCase()) {
            case "gold" -> totalGold = Math.max(0, totalGold - 1);
            case "silver" -> totalSilver = Math.max(0, totalSilver - 1);
            case "bronze" -> totalBronze = Math.max(0, totalBronze - 1);
            default -> throw new IllegalArgumentException("잘못된 메달 타입: " + medalType);
        }
    }

    // (3) 총 메달 수 계산 메서드
    // 금, 은, 동 메달의 합을 반환
    public int getTotalMedals() {
        return totalGold + totalSilver + totalBronze;
    }

    // (4) Getter 메서드
    // 속성값(국가 이름, 대륙, 메달 개수)을 반환
    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public int getTotalSilver() {
        return totalSilver;
    }

    public int getTotalBronze() {
        return totalBronze;
    }

    // (5) Setter 메서드
    // 속성값(국가 이름, 대륙)을 설정
    public void setName(String name) {
        this.name = name;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    // (6) 국가 정보를 문자열로 반환 (toString 메서드)
    @Override
    public String toString() {
        return String.format("%s (%s) - Gold: %d, Silver: %d, Bronze: %d",
                name, continent, totalGold, totalSilver, totalBronze);
    }

    // (7) equals 및 hashCode 재정의
    // 같은 이름과 대륙을 가진 국가를 동일 객체로 간주
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // 같은 객체면 true
        if (obj == null || getClass() != obj.getClass()) return false; // null 또는 다른 클래스인 경우 false

        Country country = (Country) obj;
        return name.equals(country.name) && continent.equals(country.continent); // 이름과 대륙이 같으면 동일
    }

    @Override
    public int hashCode() {
        int result = name.hashCode(); // 이름의 해시코드 사용
        result = 31 * result + continent.hashCode(); // 대륙의 해시코드 추가
        return result; // 최종 해시코드 반환
    }
}