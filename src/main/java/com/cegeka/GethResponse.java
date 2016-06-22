package com.cegeka;

import lombok.Data;

@Data
public class GethResponse {
    private Integer id;
    private String jsonrpc;
    private String result;
}
