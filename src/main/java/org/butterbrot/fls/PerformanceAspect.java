package org.butterbrot.fls;

enum PerformanceAspect {
    BLOCKS("Most Blocks Throwns", new BlocksAccessor()), CASUALTIES("Most Casualties Caused", new CasualtiesAccessor()),
    COMPLECTIONS("Most Completions Thrown", new CasualtiesAccessor()),
    FOULS("Most Fouls Committed", new FoulsAccessor()),
    INTERCEPTIONS("Most Interceptions Caught", new InterceptionsAccessor()),
    MVPS("Most MVPs Received", new MvpsAccessor()), PASSING("Most Passing Yards", new PassingAccessor()),
    RUSHING("Most Rushing Yards", new RushingAccessor()),
    TOUCHDOWNS("Most Touchdowns Scored", new TouohdownsAccessor()), TURNS("Most Turns Played", new TurnsAccessor());

    private final String fieldName;
    private final AspectAccessor accessor;


    PerformanceAspect(String fieldName, AspectAccessor accessor) {
        this.fieldName = fieldName;
        this.accessor = accessor;
    }

    Integer getValue(Performance performance) {
        return accessor.access(performance);
    }

    public String getFieldName() {
        return fieldName;
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

    private static class TouohdownsAccessor implements AspectAccessor {

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
