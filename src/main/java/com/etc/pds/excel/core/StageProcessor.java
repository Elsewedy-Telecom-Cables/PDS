package com.etc.pds.excel.core;
import java.util.List;

public interface StageProcessor<T> {
    void applyActions(List<T> items, boolean[] hasError);
}
