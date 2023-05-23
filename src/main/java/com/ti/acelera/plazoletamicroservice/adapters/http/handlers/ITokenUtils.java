package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;

import java.util.List;

public interface ITokenUtils {
    String getIdFromToken(String token);
    public List<String> getRoles(String token);
}
