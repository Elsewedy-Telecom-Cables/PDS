package com.etc.pds.excel.core;
import java.util.List;

public interface StageValidator<T> {
    List<String> validate(List<T> items);
}
