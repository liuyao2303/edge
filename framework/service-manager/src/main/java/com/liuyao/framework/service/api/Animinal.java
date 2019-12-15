package com.liuyao.framework.service.api;

import java.util.Date;
import java.util.Map;

public interface Animinal {
    int earn(int pay);

    void fee(String fee);

    Map<String,String> getMaps(Date date);
}
