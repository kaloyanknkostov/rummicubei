package com.MCTS;

import java.util.ArrayList;

public class AllSetGenerator {


    public static ArrayList<ArrayList<Integer>> generateAllSets() {
        ArrayList<ArrayList<Integer>> allSets = new ArrayList<>();

        // Generate sets without jokers
        allSets.addAll(generateSetsWithoutJokers());

        // Generate sets with one joker
        allSets.addAll(generateSetsWithOneJoker());

        // Generate sets with two jokers
        allSets.addAll(generateSetsWithTwoJokers());

        return allSets;
    }

    private static ArrayList<ArrayList<Integer>> generateSetsWithTwoJokers() {
        ArrayList<ArrayList<Integer>> allSetsWithTwoJokers = new ArrayList<>();

        // Same color 3 in a row but replace 2 with jokers
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 13; start++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    set.add(53); // Joker
                }
                allSetsWithTwoJokers.add(set);
            }
        }

        // Same color 4 in a row but replace 2 with jokers
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 10; start++) {
                for (int jokerPos1 = 1; jokerPos1 <= 4; jokerPos1++) {
                    for (int jokerPos2 = jokerPos1 + 1; jokerPos2 <= 4; jokerPos2++) {
                        ArrayList<Integer> set = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            if (i == jokerPos1 - 1 || i == jokerPos2 - 1) {
                                set.add(53); // Joker
                            } else {
                                set.add((color - 1) * 13 + start + i);
                            }
                        }
                        allSetsWithTwoJokers.add(set);
                    }
                }
            }
        }

        // Same color 5 in a row but replace 2 with jokers
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 9; start++) {
                for (int jokerPos1 = 1; jokerPos1 <= 5; jokerPos1++) {
                    for (int jokerPos2 = jokerPos1 + 1; jokerPos2 <= 5; jokerPos2++) {
                        ArrayList<Integer> set = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            if (i == jokerPos1 - 1 || i == jokerPos2 - 1) {
                                set.add(53); // Joker
                            } else {
                                set.add((color - 1) * 13 + start + i);
                            }
                        }
                        allSetsWithTwoJokers.add(set);
                    }
                }
            }
        }

        // Same number 4 but replace 2 with jokers
        for (int num = 1; num <= 13; num++) {
            for (int jokerPos1 = 1; jokerPos1 <= 4; jokerPos1++) {
                for (int jokerPos2 = jokerPos1 + 1; jokerPos2 <= 4; jokerPos2++) {
                    ArrayList<Integer> set = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        if (i == jokerPos1 - 1 || i == jokerPos2 - 1) {
                            set.add(53); // Joker
                        } else {
                            set.add(num + i * 13);
                        }
                    }
                    allSetsWithTwoJokers.add(set);
                }
            }
        }

        return allSetsWithTwoJokers;
    }

    private static ArrayList<ArrayList<Integer>> generateSetsWithoutJokers() {
        ArrayList<ArrayList<Integer>> allSetsWithoutJokers = new ArrayList<>();

        // Same color 3 in a row
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 11; start++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    set.add((color - 1) * 13 + start + i);
                }
                allSetsWithoutJokers.add(set);
            }
        }

        // Same color 4 in a row
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 10; start++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    set.add((color - 1) * 13 + start + i);
                }
                allSetsWithoutJokers.add(set);
            }
        }

        // Same color 5 in a row
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 9; start++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    set.add((color - 1) * 13 + start + i);
                }
                allSetsWithoutJokers.add(set);
            }
        }

        // Same number 3
        for (int num = 1; num <= 13; num++) {
            ArrayList<Integer> set = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                set.add(num + i * 13);
            }
            allSetsWithoutJokers.add(set);
        }

        // Same number 4
        for (int num = 1; num <= 13; num++) {
            ArrayList<Integer> set = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                set.add(num + i * 13);
            }
            allSetsWithoutJokers.add(set);
        }

        return allSetsWithoutJokers;
    }

    private static ArrayList<ArrayList<Integer>> generateSetsWithOneJoker() {
        ArrayList<ArrayList<Integer>> allSetsWithOneJoker = new ArrayList<>();

        // Same color 3 in a row but replace 1 of the tiles with a joker
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 11; start++) {
                for (int jokerPos = 1; jokerPos <= 3; jokerPos++) {
                    ArrayList<Integer> set = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        if (i == jokerPos - 1) {
                            set.add(53); // Joker
                        } else {
                            set.add((color - 1) * 13 + start + i);
                        }
                    }
                    allSetsWithOneJoker.add(set);
                }
            }
        }

        // Same color 4 in a row but replace 1 with a joker
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 10; start++) {
                for (int jokerPos = 1; jokerPos <= 4; jokerPos++) {
                    ArrayList<Integer> set = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        if (i == jokerPos - 1) {
                            set.add(53); // Joker
                        } else {
                            set.add((color - 1) * 13 + start + i);
                        }
                    }
                    allSetsWithOneJoker.add(set);
                }
            }
        }

        // Same color 5 in a row but replace 1 with a joker
        for (int color = 1; color <= 4; color++) {
            for (int start = 1; start <= 9; start++) {
                for (int jokerPos = 1; jokerPos <= 5; jokerPos++) {
                    ArrayList<Integer> set = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        if (i == jokerPos - 1) {
                            set.add(53); // Joker
                        } else {
                            set.add((color - 1) * 13 + start + i);
                        }
                    }
                    allSetsWithOneJoker.add(set);
                }
            }
        }

        // Same number 3 but replace 1 with a joker
        for (int num = 1; num <= 13; num++) {
            for (int jokerPos1 = 1; jokerPos1 <= 2; jokerPos1++) {
                for (int jokerPos2 = jokerPos1 + 1; jokerPos2 <= 3; jokerPos2++) {
                    ArrayList<Integer> set = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        if (i == jokerPos1 - 1 || i == jokerPos2 - 1) {
                            set.add(53); // Joker
                        } else {
                            set.add(num + i * 13);
                        }
                    }
                    allSetsWithOneJoker.add(set);
                }
            }
        }

        // Same number 4 but replace 1 with a joker
        for (int num = 1; num <= 13; num++) {
            for (int jokerPos = 1; jokerPos <= 4; jokerPos++) {
                ArrayList<Integer> set = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    if (i == jokerPos - 1) {
                        set.add(53); // Joker
                    } else {
                        set.add(num + i * 13);
                    }
                }
                allSetsWithOneJoker.add(set);
            }
        }

        return allSetsWithOneJoker;
    }
}
