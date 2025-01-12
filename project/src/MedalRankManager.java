import java.io.*;
import java.util.*;

public class MedalRankManager implements Rankable {
    private List<OlympicCountry> countries; // 국가 리스트를 저장하는 필드

    // 생성자: 국가 리스트 초기화
    public MedalRankManager() {
        this.countries = new ArrayList<>();
    }

    // 1. 국가 추가
    // 새로운 국가를 리스트에 추가
    public void addCountry(OlympicCountry country) {
        countries.add(country);
    }

    // 2. 종목별 메달 순위 계산
    @Override
    public void calculateRankByEvent(String event) {
        System.out.println("\n===== 종목별 메달 순위: " + event + " =====");

        countries.stream()
                .filter(c -> c.getEvents().contains(event)) // 해당 종목에 참가한 국가 필터링
                .sorted((c1, c2) -> { // 금 > 은 > 동 순서로 정렬
                    int goldDiff = c2.getMedalsByEvent(event, "gold") - c1.getMedalsByEvent(event, "gold");
                    if (goldDiff != 0) return goldDiff;

                    int silverDiff = c2.getMedalsByEvent(event, "silver") - c1.getMedalsByEvent(event, "silver");
                    if (silverDiff != 0) return silverDiff;

                    return c2.getMedalsByEvent(event, "bronze") - c1.getMedalsByEvent(event, "bronze");
                })
                .forEach(c -> System.out.printf("%s - 금: %d, 은: %d, 동: %d\n",
                        c.getName(),
                        c.getMedalsByEvent(event, "gold"),
                        c.getMedalsByEvent(event, "silver"),
                        c.getMedalsByEvent(event, "bronze")));
    }

    // 3. 총합 메달 순위 계산
    @Override
    public void calculateTotalRank() {
        System.out.println("\n===== 총합 메달 순위 =====");

        countries.stream()
                .sorted((c1, c2) -> { // 금 > 은 > 동 순으로 정렬
                    int goldDiff = c2.getTotalGold() - c1.getTotalGold();
                    if (goldDiff != 0) return goldDiff;

                    int silverDiff = c2.getTotalSilver() - c1.getTotalSilver();
                    if (silverDiff != 0) return silverDiff;

                    return c2.getTotalBronze() - c1.getTotalBronze();
                })
                .forEach(c -> System.out.printf("%s - 금: %d, 은: %d, 동: %d (총합: %d)\n",
                        c.getName(), c.getTotalGold(), c.getTotalSilver(), c.getTotalBronze(),
                        c.getTotalMedals()));
    }

    // 4. 파일에서 국가 데이터 로드
    public void loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            // 파일 데이터를 ','로 분리
            String[] parts = line.split(",");
            String name = parts[0]; // 국가 이름
            String continent = parts[1]; // 대륙 이름
            List<String> events = Arrays.asList(parts[2].split(";")); // 참가 종목

            OlympicCountry country = new OlympicCountry(name, continent, events);

            // 메달 데이터가 존재하는 경우 처리
            if (parts.length > 3) {
                String[] medalEntries = parts[3].split("\\|");
                for (String medalEntry : medalEntries) {
                    String[] medalParts = medalEntry.split(":");
                    String event = medalParts[0];
                    String[] medalCounts = medalParts[1].split(";");

                    int gold = Integer.parseInt(medalCounts[0]);
                    int silver = Integer.parseInt(medalCounts[1]);
                    int bronze = Integer.parseInt(medalCounts[2]);

                    // 메달 개수에 따라 메달 추가
                    try {
                        for (int i = 0; i < gold; i++) country.addMedalByEvent(event, "gold");
                        for (int i = 0; i < silver; i++) country.addMedalByEvent(event, "silver");
                        for (int i = 0; i < bronze; i++) country.addMedalByEvent(event, "bronze");
                    } catch (InvalidEventException e) {
                        System.out.println("오류: " + e.getMessage());
                    }
                }
            }

            // 국가를 리스트에 추가
            addCountry(country);
        }

        reader.close(); // 파일 읽기 종료
    }

    // 5. 국가 데이터를 파일에 저장
    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        for (OlympicCountry country : countries) {
            // 1. 종목별 메달 데이터 작성
            StringBuilder medalData = new StringBuilder();
            for (String event : country.getEvents()) {
                Map<String, Integer> medals = country.getMedalsByEventMap(event); // 종목별 메달 데이터 가져오기
                medalData.append(event).append(":")
                        .append(medals.getOrDefault("gold", 0)).append(";")
                        .append(medals.getOrDefault("silver", 0)).append(";")
                        .append(medals.getOrDefault("bronze", 0)).append("|");
            }

            // 2. 국가 데이터 작성 및 저장
            writer.write(country.getName() + "," +
                    country.getContinent() + "," +
                    String.join(";", country.getEvents()) + "," +
                    medalData.toString());
            writer.newLine();
        }

        writer.close(); // 파일 저장 종료
    }

    // 6. 국가 조회 메서드
    // 국가 이름을 기준으로 OlympicCountry 객체를 반환
    public OlympicCountry findCountryByName(String name) throws InvalidCountryException {
        for (OlympicCountry country : countries) {
            if (country.getName().equalsIgnoreCase(name)) {
                return country; // 국가를 찾으면 반환
            }
        }
        // 국가를 찾지 못한 경우 예외 발생
        throw new InvalidCountryException(name + "는 존재하지 않는 국가입니다.");
    }

    // 7. 대륙별 메달 순위 계산
    public void calculateRankByContinent(String continent) {
        System.out.println("\n===== " + continent + " 메달 순위 =====");

        countries.stream()
                .filter(c -> c.getContinent().equals(continent)) // 대륙별 국가 필터링
                .sorted((c1, c2) -> { // 금 > 은 > 동 순으로 정렬
                    int goldDiff = c2.getTotalGold() - c1.getTotalGold();
                    if (goldDiff != 0) return goldDiff;

                    int silverDiff = c2.getTotalSilver() - c1.getTotalSilver();
                    if (silverDiff != 0) return silverDiff;

                    return c2.getTotalBronze() - c1.getTotalBronze();
                })
                .forEach(c -> System.out.printf("%s - 금: %d, 은: %d, 동: %d (총합: %d)\n",
                        c.getName(), c.getTotalGold(), c.getTotalSilver(), c.getTotalBronze(),
                        c.getTotalMedals()));
    }
}