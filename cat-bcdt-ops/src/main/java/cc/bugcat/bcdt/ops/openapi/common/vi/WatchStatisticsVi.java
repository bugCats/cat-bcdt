package cc.bugcat.bcdt.ops.openapi.common.vi;

import lombok.Data;

import java.util.Map;

@Data
public class WatchStatisticsVi {
        
    private Map<String, Integer> r3m;

    private Map<String, Integer> r10m;

    private Map<String, Integer> r30m;

    private Map<String, Integer> r1h;

    private Map<String, Integer> r2h;

    private Map<String, Integer> r4h;

    private Map<String, Integer> r8h;


}
