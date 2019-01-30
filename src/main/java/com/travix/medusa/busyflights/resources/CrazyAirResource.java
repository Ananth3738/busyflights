package com.travix.medusa.busyflights.resources;

import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.repository.CrazyAirFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/supplier/service/crazyair")
public class CrazyAirResource {

    @Autowired
    private CrazyAirFlightRepository crazyAirFlightRepository;

    @GetMapping("/getCrazyAirFlights")
    public List<CrazyAirResponse> fetchCrazYAirDtls(@RequestBody final CrazyAirRequest crazyAirRequest){
        List<CrazyAirResponse> crazyAirResponses = new ArrayList<CrazyAirResponse>();

        return crazyAirResponses;
    }
}
