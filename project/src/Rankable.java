public interface Rankable {
    void calculateRankByEvent(String event); // 종목별 순위
    void calculateTotalRank(); // 총합 순위
    void calculateRankByContinent(String continent); // 대륙별 순위
}