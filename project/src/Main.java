/*
객체지향 프로그래밍 평가과제
학과: 컴퓨터공학과
학번: 202104522
이름: 이은진

과제 주제: 올림픽 메달 관리 시스템
 */

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        // 입력을 받기 위한 Scanner 객체와 MedalRankManager 초기화
        Scanner scanner = new Scanner(System.in);
        MedalRankManager manager = new MedalRankManager();
        boolean running = true; // 프로그램 실행 상태 플래그

        // 1. 파일에서 초기 데이터 로드
        try {
            manager.loadFromFile("countries.txt");
            System.out.println("기존 데이터를 성공적으로 불러왔습니다.");
        } catch (IOException e) {
            System.out.println("데이터 파일을 불러오는 중 오류 발생: " + e.getMessage());
        }

        // 2. 메인 메뉴 실행
        while (running) {
            showMenu(); // 메뉴 출력
            int choice = scanner.nextInt(); // 사용자 입력
            scanner.nextLine(); // 입력 버퍼 비우기

            // 사용자 선택에 따라 작업 실행
            switch (choice) {
                case 1 -> addMedal(manager, scanner);        // 메달 추가
                case 2 -> removeMedal(manager, scanner);     // 메달 삭제
                case 3 -> rankByEvent(manager, scanner);     // 종목별 순위 조회
                case 4 -> rankByTotal(manager);              // 총합 순위 조회
                case 5 -> rankByContinent(manager, scanner); // 대륙별 순위 조회
                case 6 -> { // 종료
                    running = false;
                    System.out.println("프로그램을 종료합니다.");
                }
                default -> System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }

        // 3. 프로그램 종료 시 데이터 저장
        try {
            manager.saveToFile("countries.txt");
            System.out.println("데이터를 파일에 성공적으로 저장했습니다.");
        } catch (IOException e) {
            System.out.println("데이터 저장 중 오류 발생: " + e.getMessage());
        }

        scanner.close(); // Scanner 리소스 해제
    }

    // 메뉴를 출력하는 메서드
    private static void showMenu() {
        System.out.println("\n===== 올림픽 메달 관리 시스템 =====");
        System.out.println("1. 메달 추가");
        System.out.println("2. 메달 삭제");
        System.out.println("3. 종목별 메달 순위 조회");
        System.out.println("4. 총합 메달 순위 조회");
        System.out.println("5. 대륙별 메달 순위 조회");
        System.out.println("6. 종료");
        System.out.print("메뉴를 선택하세요: ");
    }

    // 1. 메달 추가 메서드
    private static void addMedal(MedalRankManager manager, Scanner scanner) {
        try {
            System.out.print("메달을 추가할 국가 이름을 입력하세요: ");
            String countryName = scanner.nextLine();
            OlympicCountry country = manager.findCountryByName(countryName); // 예외 발생 가능

            System.out.print("종목 이름을 입력하세요: ");
            String event = scanner.nextLine();

            // 종목 유효성 검사
            if (!country.getEvents().contains(event)) {
                System.out.println("오류: " + event + "는 참가하지 않은 종목입니다.");
                return;
            }

            System.out.println("메달 종류를 선택하세요:");
            System.out.println("1. 금메달");
            System.out.println("2. 은메달");
            System.out.println("3. 동메달");
            int medalChoice = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼 비우기

            String medalType = switch (medalChoice) {
                case 1 -> "gold";
                case 2 -> "silver";
                case 3 -> "bronze";
                default -> "invalid";
            };

            if (medalType.equals("invalid")) {
                System.out.println("잘못된 선택입니다.");
                return;
            }

            // 메달 추가
            country.addMedalByEvent(event, medalType);
            System.out.println("메달이 성공적으로 추가되었습니다.");

        } catch (InvalidCountryException e) {
            System.out.println("오류: " + e.getMessage()); // 잘못된 국가 처리
        } catch (InvalidEventException e) {
            System.out.println("오류: " + e.getMessage()); // 잘못된 종목 처리
        }
    }


    // 2. 메달 삭제 메서드
    private static void removeMedal(MedalRankManager manager, Scanner scanner) {
        try {
            System.out.print("메달을 삭제할 국가 이름을 입력하세요: ");
            String countryName = scanner.nextLine();
            OlympicCountry country = manager.findCountryByName(countryName); // 예외 발생 가능

            System.out.print("종목 이름을 입력하세요: ");
            String event = scanner.nextLine();

            // 종목 유효성 검사
            if (!country.getEvents().contains(event)) {
                System.out.println("오류: " + event + "는 참가하지 않은 종목입니다.");
                return;
            }

            System.out.println("메달 종류를 선택하세요:");
            System.out.println("1. 금메달");
            System.out.println("2. 은메달");
            System.out.println("3. 동메달");
            int medalChoice = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼 비우기

            String medalType = switch (medalChoice) {
                case 1 -> "gold";
                case 2 -> "silver";
                case 3 -> "bronze";
                default -> "invalid";
            };

            if (medalType.equals("invalid")) {
                System.out.println("잘못된 선택입니다.");
                return;
            }

            // 메달 삭제
            country.removeMedalByEvent(event, medalType);
            System.out.println("메달이 성공적으로 삭제되었습니다.");

        } catch (InvalidCountryException e) {
            System.out.println("오류: " + e.getMessage()); // 잘못된 국가 처리
        } catch (InvalidEventException e) {
            System.out.println("오류: " + e.getMessage()); // 잘못된 종목 처리
        }
    }

    // 3. 종목별 메달 순위 조회 메서드
    private static void rankByEvent(MedalRankManager manager, Scanner scanner) {
        System.out.print("조회할 종목 이름을 입력하세요: ");
        String event = scanner.nextLine(); // 조회할 종목 입력
        manager.calculateRankByEvent(event); // 순위 계산
    }

    // 4. 총합 메달 순위 조회 메서드
    private static void rankByTotal(MedalRankManager manager) {
        manager.calculateTotalRank(); // 순위 계산
    }

    // 5. 대륙별 메달 순위 조회 메서드
    private static void rankByContinent(MedalRankManager manager, Scanner scanner) {
        System.out.println("\n===== 대륙별 메달 순위 =====");
        System.out.println("1. 아시아");
        System.out.println("2. 북아메리카");
        System.out.println("3. 유럽");
        System.out.println("4. 남아메리카");
        System.out.print("대륙을 선택하세요: ");
        int choice = scanner.nextInt(); // 대륙 선택
        scanner.nextLine(); // 입력 버퍼 비우기

        // 선택된 대륙 매핑
        String continent = switch (choice) {
            case 1 -> "아시아";
            case 2 -> "북아메리카";
            case 3 -> "유럽";
            case 4 -> "남아메리카";
            default -> "invalid";
        };

        if (continent.equals("invalid")) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        manager.calculateRankByContinent(continent); // 대륙별 순위 계산
    }
}