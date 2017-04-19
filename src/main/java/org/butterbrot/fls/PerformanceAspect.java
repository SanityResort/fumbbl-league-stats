package org.butterbrot.fls;

enum PerformanceAspect {
    BLOCKS("Most Blocks Thrown", new BlocksAccessor(), "Played Turns", new TurnsAccessor()),
    CASUALTIES("Most Casualties Caused", new CasualtiesAccessor(), "Blocks Thrown", new BlocksAccessor()),
    COMPLETIONS("Most Completions Thrown", new CompletionsAccessor(), false, "Yards Passed", new PassingAccessor()),
    FOULS("Most Fouls Committed", new FoulsAccessor(), "Played Turns", new TurnsAccessor()),
    INTERCEPTIONS("Most Interceptions Caught", new InterceptionsAccessor(), "Played Turns", new TurnsAccessor()),
    MVPS("Most MVPs Received", new MvpsAccessor()),
    PASSING("Most Passing Yards", new PassingAccessor(), "Completions Thrown", new CompletionsAccessor()),
    RUSHING("Most Rushing Yards", new RushingAccessor(), "Played Turns", new TurnsAccessor()),
    SPPS("Most Spps Earned", new SppsAccessor(), "MVPs Received", new MvpsAccessor()),
    TOUCHDOWNS("Most Touchdowns Scored", new TouchdownsAccessor(), "Played Turns", new TurnsAccessor()),
    TURNS("Most Turns Played", new TurnsAccessor());

    private final String title;
    private final AspectAccessor accessor;
    private boolean sortTiebreakAsc;
    private String tieBreakerTitle;
    private AspectAccessor tieBreakerAccessor;

    PerformanceAspect(String title, AspectAccessor accessor, boolean sortTiebreakAsc, String tieBreakerTitle, AspectAccessor
            tieBreakerAccessor) {
        this.title = title;
        this.accessor = accessor;
        this.sortTiebreakAsc = sortTiebreakAsc;
        this.tieBreakerTitle = tieBreakerTitle;
        this.tieBreakerAccessor = tieBreakerAccessor;
    }

    PerformanceAspect(String title, AspectAccessor accessor, String tieBreakerTitle, AspectAccessor tieBreakerAccessor) {
        this(title, accessor, true, tieBreakerTitle, tieBreakerAccessor);
    }

    PerformanceAspect(String title, AspectAccessor accessor) {
        this(title, accessor, true, null, null);
    }

    Integer getValue(Performance performance) {
        return accessor.access(performance);
    }

    Integer getTieBreakerValue(Performance performance) {return hasTiebreaker()?tieBreakerAccessor.access(performance): null;}

    public String getTitle() {
        return title;
    }

    public boolean hasTiebreaker() {
        return tieBreakerAccessor != null;
    }

    public boolean isSortTiebreakAsc() {
        return sortTiebreakAsc;
    }

    public String getTieBreakerTitle() {
        return tieBreakerTitle;
    }

    private interface AspectAccessor {

        int access(Performance performance);
    }

    private static class BlocksAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getBlocks();
        }
    }

    private static class CasualtiesAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getCasualties();
        }
    }

    private static class CompletionsAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getCompletions();
        }
    }

    private static class FoulsAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getFouls();
        }
    }

    private static class InterceptionsAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getInterceptions();
        }
    }

    private static class MvpsAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getMvps();
        }
    }

    private static class PassingAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getPassing();
        }
    }

    private static class RushingAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getRushing();
        }
    }

    private static class SppsAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getSpp();
        }
    }

    private static class TouchdownsAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getTouchdowns();
        }
    }

    private static class TurnsAccessor implements AspectAccessor {

        @Override
        public int access(Performance performance) {
            return performance.getTurns();
        }
    }


}
