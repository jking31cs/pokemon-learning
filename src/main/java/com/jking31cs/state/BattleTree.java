package com.jking31cs.state;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jking31 on 11/1/16.
 */
public class BattleTree {
    public Map<Long, State> states = new HashMap<>();
    public Map<Long, Edge> edges = new HashMap<>();
}
