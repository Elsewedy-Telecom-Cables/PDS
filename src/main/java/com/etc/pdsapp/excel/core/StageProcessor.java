package com.etc.pdsapp.excel.core;
import java.util.List;

public interface StageProcessor<T> {
    void applyActions(List<T> items);
}
