package com.api_league.retrofit;
import com.api_league.models.EventResponse;
import com.api_league.models.LeaguesResponse;
import com.api_league.models.StandingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SportsApiService {

    @GET("all_leagues.php")
    Call<LeaguesResponse> getAllLeagues();

    @GET("search_all_leagues.php")
    Call<LeaguesResponse> getLeaguesByCountry(@Query("c") String country);

    @GET("lookuptable.php")
    Call<StandingResponse> getStandings(@Query("l") String idLeague,@Query("s") String season);

    @GET("eventsround.php")
    Call<EventResponse> getEvents(@Query("id") String idLeague, @Query("r") String ronda, @Query("s") String season);
}
