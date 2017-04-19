package org.butterbrot.fls;

enum PerformanceAspect {
    BLOCKS("blocks", new BlocksAccessor()), CASUALTIES("casualties", new CasualtiesAccessor()), COMPLECTIONS("completions", new CasualtiesAccessor()),
    FOULS("fouls", new FoulsAccessor()), INTERCEPTIONS("interceptions", new InterceptionsAccessor()), MVPS("mvps", new MvpsAccessor()),
    PASSING("passing", new PassingAccessor()), RUSHING("rushing", new RushingAccessor()), TOUCHDOWNS("touchdowns", new TouohdownsAccessor()), TURNS("turns", new TurnsAccessor());

    private final String fieldName;
    private final AspectAccessor accessor;


    PerformanceAspect(String fieldName, AspectAccessor accessor) {
        this.fieldName = fieldName;
        this.accessor = accessor;
    }

    Integer getValue(Performance performance){
        return accessor.access(performance);
    }

    public String getFieldName() {
        return fieldName;
    }

    private interface AspectAccessor {

        int access(Performance performance);
    }

    private static class BlocksAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getBlocks();
        }
    }

    private static class CasualtiesAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getCasualties();
        }
    }

    private static class CompletionsAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getCompletions();
        }
    }

    private static class FoulsAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getFouls();
        }
    }

    private static class InterceptionsAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getInterceptions();
        }
    }

    private static class MvpsAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getMvps();
        }
    }

    private static class PassingAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getPassing();
        }
    }

    private static class RushingAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getRushing();
        }
    }

    private static class TouohdownsAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getTouchdowns();
        }
    }

    private static class TurnsAccessor implements AspectAccessor{

        @Override
        public int access(Performance performance) {
            return performance.getTurns();
        }
    }



}
