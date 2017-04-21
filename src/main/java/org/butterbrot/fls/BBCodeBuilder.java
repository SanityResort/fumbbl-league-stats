package org.butterbrot.fls;

public class BBCodeBuilder {
    private static String COLOR_BRONZE = "#CD7F32";
    private static String COLOR_SILVER = "silver";
    private static String COLOR_GOLD = "gold";
    private static String COLOR_EVEN = "#e6ddc7";
    private static String COLOR_ODD = "#d6cdb7";
    private static String TAG_TH = "th";
    private static String TAG_TD = "td";
    private static String TEMPLATE_CELL = "  [%s=blackborder align=center]%s[/%s]\\%n";
    private static String TEMPLATE_PLAYER_URL = "[url=https://fumbbl.com/p/player?player_id=%d]%s[/url]";
    private static String TEMPLATE_TEAM_URL = "[url=https://fumbbl.com%s]%s[/url]";
    private static String TEMPLATE_HEADER = "[table blackborder border2 width=50%%][tr bg=#392]\\%n  [td blackborder "
            + "colspan=%d align=center][color=white]%s[/color][/td]\\%n";
    private static String TEMPLATE_ROW_BREAK = "[/tr][tr bg=%s]\\%n";

    public void populate(PerformancesWrapper wrapper) {
        boolean haaTiebreaker = wrapper.getTiebreakerTitle() != null;
        int size =  wrapper.getPerformances().size();
        StringBuilder builder = new StringBuilder();
        builder.append(tableHeader("Most " + wrapper.getTitle(), haaTiebreaker ? 5 : 4));
        builder.append(rowBreakNoColor());
        builder.append(cell(TAG_TH, "#"));
        builder.append(cell(TAG_TH, "Player"));
        builder.append(cell(TAG_TH, "Team"));
        builder.append(cell(TAG_TH, wrapper.getTitle()));
        if (haaTiebreaker) {
            builder.append(cell(TAG_TH, wrapper.getTiebreakerTitle()));
        }
        if (size>0) {
            builder.append(contentRow(COLOR_GOLD, haaTiebreaker, wrapper.getPerformances().get(0)));
        }
        if (size>1) {
            builder.append(contentRow(COLOR_SILVER, haaTiebreaker, wrapper.getPerformances().get(1)));
        }
        if (size>2){
            builder.append(contentRow(COLOR_BRONZE, haaTiebreaker, wrapper.getPerformances().get(2)));
        }
        for (int i = 3; i < size; i++) {
            if (i % 2 == 0) {
                builder.append(contentRow(COLOR_ODD, haaTiebreaker, wrapper.getPerformances().get(i)));
            } else {
                builder.append(contentRow(COLOR_EVEN, haaTiebreaker, wrapper.getPerformances().get(i)));
            }
        }

        builder.append(tableFooter());
        wrapper.setBbCode(builder.toString());
    }


    private String cell(String tag, String content) {
        return String.format(TEMPLATE_CELL, tag, content, tag);
    }

    private String playerUrl(String name, int id) {
        return String.format(TEMPLATE_PLAYER_URL, id, name);
    }

    private String teamUrl(String name, String urlPath) {
        return String.format(TEMPLATE_TEAM_URL, urlPath, name);
    }

    private String tableHeader(String title, int colspan) {
        return String.format(TEMPLATE_HEADER, colspan, title);
    }

    private String tableFooter() {
        return "[/tr][/table]";
    }

    private String rowBreakNoColor() {
        return "[/tr][tr]\\\n";
    }

    private String rowBreak(String color) {
        return String.format(TEMPLATE_ROW_BREAK, color);
    }

    private String contentRow(String color, boolean hasTiebreaker, PerformanceValue performanceValue) {
        return rowBreak(color) + contentCells(hasTiebreaker, performanceValue);
    }

    private String contentRow(boolean hasTiebreaker, PerformanceValue performanceValue) {
        return rowBreakNoColor() + contentCells(hasTiebreaker, performanceValue);
    }

    private String contentCells(boolean hasTiebreaker, PerformanceValue performanceValue) {
        StringBuilder builder = new StringBuilder();
        builder.append(cell(TAG_TD, String.valueOf(performanceValue.getPlace())));
        builder.append(cell(TAG_TD, playerUrl(performanceValue.getPlayerName(), performanceValue.getPlayerId())));
        builder.append(cell(TAG_TD, teamUrl(performanceValue.getTeamName(), performanceValue.getTeamName())));
        builder.append(cell(TAG_TD, String.valueOf(performanceValue.getValue())));
        if (hasTiebreaker) {
            builder.append(cell(TAG_TD, String.valueOf(performanceValue.getTiebreaker())));
        }
        return builder.toString();
    }
}
/*

[table blackborder border2 width=50%][tr bg=#392]\
  [td blackborder colspan=4 align=center][color=white]Most Casualties Caused[/color][/td]\
[/tr][tr]\
  [th=blackborder align=center]#[/th]\
  [th=blackborder align=center]Player[/th]\
  [th=blackborder align=center]Team[/th]\
  [th=blackborder align=center]Casualties[/th]\
[/tr][tr bg=gold]\
  [td=blackborder align=center]1[/td]\
  [td=blackborder align=center][url=https://fumbbl.com/p/player?player_id=10086364]Bloods Hummelskin[/url][/td]\
  [td=blackborder align=center]R1C3[/td]\
  [td=blackborder align=center]R1C4[/td]\
[/tr][tr bg=silver]\
  [td=blackborder align=center]2[/td]\
  [td=blackborder align=center]R1C2[/td]\
  [td=blackborder align=center]Accrington Stanley Originals[/td]\
  [td=blackborder align=center]R1C4[/td]\
[/tr][tr bg=#CD7F32]\
  [td=blackborder align=center]3[/td]\
  [td=blackborder align=center]R1C2[/td]\
  [td=blackborder align=center]R1C3[/td]\
  [td=blackborder align=center]R1C4[/td]\
[/tr][tr]\
  [td=blackborder align=center]4[/td]\
  [td=blackborder align=center]R1C2[/td]\
  [td=blackborder align=center]R1C3[/td]\
  [td=blackborder align=center]R1C4[/td]\
[/tr][tr]\
  [td=blackborder align=center]5[/td]\
  [td=blackborder align=center]R1C2[/td]\
  [td=blackborder align=center]R1C3[/td]\
  [td=blackborder align=center]R1C4[/td]\
[/tr][/table]

<table class="wrapper" th:each="wrapper: ${wrappers}">
    <tbody>
    <tr class="title">
        <td th:text="'Most ' + ${wrapper.title}" colspan="5" class="padded"></td>
    </tr>
    <tr class="header">
        <th class="bordered">#</th>
        <th class="bordered">Player</th>
        <th class="bordered">Team</th>
        <th class="bordered" th:text="${wrapper.title}"></th>
        <th class="bordered" th:if="${wrapper.tiebreakerTitle}" th:text="${wrapper.tiebreakerTitle}"></th>
    </tr>
    <tr th:each="performance, indexStat: ${wrapper.performances}"
        th:class="${indexStat.count==1}? 'gold' : (${indexStat.count==2}?'silver': (${indexStat.count==3}?'bronze':
        (${indexStat.even}? 'even')))">
        <td class="bordered" th:text="${performance.place}"></td>
        <td class="bordered"><a th:text="${performance.playerName}"
               th:href="'https://fumbbl.com/p/player?player_id='+${performance.playerId}"></a>
        </td>
        <td
        class="bordered
        "><a th:text="${performance.teamName}" th:href="'https://fumbbl.com'+${performance.teamUrlPath}"></a></td>
        <td class="bordered" th:text="${performance.value}"></td>
        <td class="bordered" th:if="${wrapper.tiebreakerTitle}" th:text="${performance.tiebreaker}"></td>
    </tr>
    </tbody>
</table>


 */