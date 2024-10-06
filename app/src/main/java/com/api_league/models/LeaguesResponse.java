package com.api_league.models;

import java.util.List;

public class LeaguesResponse {
    private List<League> leagues;
    private List<League> countries;


    public List<League> getCountries() { return countries; }
    public void setCountries(List<League> leagues) { this.countries = leagues; }

    public List<League> getLeagues() { return leagues; }
    public void setLeagues(List<League> leagues) { this.leagues = leagues; }
}
