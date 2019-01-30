package com.travix.medusa.busyflights.resources;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service/busyflights")
public class BusyFlightsResource {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/getflights", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BusyFlightsResponse> fetchBusyFlightDtls(@RequestBody final BusyFlightsRequest busyFlightsRequest) {
        //validate request
        List<BusyFlightsResponse> responses = new ArrayList<BusyFlightsResponse>();

        List<CrazyAirResponse> crazyAirResponses = invokeEasyAirService(busyFlightsRequest);
        List<ToughJetResponse> toughJetResponses = invokeToughJetResponse(busyFlightsRequest);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a z");

        for (CrazyAirResponse crazyair : crazyAirResponses) {
            BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
            busyFlightsResponse.setSupplier("CrazyAir");
            busyFlightsResponse.setAirLine(crazyair.getAirline());
            busyFlightsResponse.setArrivalDate(LocalDate.parse(crazyair.getArrivalDate(), formatter));
            busyFlightsResponse.setDepartureDate(LocalDate.parse(crazyair.getDepartureDate(), formatter));
            busyFlightsResponse.setDepartureAirportCode(crazyair.getDepartureAirportCode());
            busyFlightsResponse.setDestinationAirportCode(crazyair.getDestinationAirportCode());
//            busyFlightsResponse.setDiscount(crazyair.g);
            busyFlightsResponse.setFare(crazyair.getPrice());
            responses.add(busyFlightsResponse);
        }

        for (ToughJetResponse toughJet : toughJetResponses) {
            BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
            busyFlightsResponse.setSupplier("ToughJet");
            busyFlightsResponse.setAirLine(toughJet.getAirLine());
            busyFlightsResponse.setArrivalDate(LocalDate.parse(toughJet.getInboundDateTime(), formatter));
            busyFlightsResponse.setDepartureDate(LocalDate.parse(toughJet.getOutboundDateTime(), formatter));
            busyFlightsResponse.setDepartureAirportCode(toughJet.getDepartureAirportName());
            busyFlightsResponse.setDestinationAirportCode(toughJet.getArrivalAirportName());
            busyFlightsResponse.setDiscount(toughJet.getDiscount());
            busyFlightsResponse.setFare(toughJet.getBasePrice());
            responses.add(busyFlightsResponse);
        }

        responses = responses.stream()
                .sorted(Comparator.comparing(BusyFlightsResponse::getFare))
                .collect(Collectors.toList());

        return responses;
    }

    private List<CrazyAirResponse> invokeEasyAirService(BusyFlightsRequest busyFlightsRequest) {
        HttpEntity<CrazyAirRequest> httpCrazyAirEntity = new HttpEntity<CrazyAirRequest>(populateCrazyAirRequest(busyFlightsRequest));
        ResponseEntity<List<CrazyAirResponse>> crazyairResponses = restTemplate.exchange("http://9090/supplier/service/crazyair/getCrazyAirFlights", HttpMethod.GET, httpCrazyAirEntity, new ParameterizedTypeReference<List<CrazyAirResponse>>() {
        });
        return crazyairResponses.getBody();
    }

    private List<ToughJetResponse> invokeToughJetResponse(BusyFlightsRequest busyFlightsRequest) {
        HttpEntity<ToughJetRequest> httpToughJetEntity = new HttpEntity<ToughJetRequest>(populateToughJetRequest(busyFlightsRequest));
        ResponseEntity<List<ToughJetResponse>> toughjetResponses = restTemplate.exchange("http://9090/supplier/service/toughtjet/getToughJetFlights", HttpMethod.GET, httpToughJetEntity, new ParameterizedTypeReference<List<ToughJetResponse>>() {
        });
        return toughjetResponses.getBody();
    }

    private CrazyAirRequest populateCrazyAirRequest(BusyFlightsRequest busyFlightsRequest) {
        CrazyAirRequest crazyAirRequest = new CrazyAirRequest();
        crazyAirRequest.setDepartureDate(busyFlightsRequest.getDepartureDate());
        crazyAirRequest.setDestination(busyFlightsRequest.getDestination());
        crazyAirRequest.setOrigin(busyFlightsRequest.getOrigin());
        crazyAirRequest.setReturnDate(busyFlightsRequest.getReturnDate());
        crazyAirRequest.setPassengerCount(busyFlightsRequest.getNumberOfPassengers());
        return crazyAirRequest;
    }

    private ToughJetRequest populateToughJetRequest(BusyFlightsRequest busyFlightsRequest) {
        ToughJetRequest toughJetRequest = new ToughJetRequest();
        toughJetRequest.setOutboundDate(busyFlightsRequest.getDepartureDate());
        toughJetRequest.setTo(busyFlightsRequest.getDestination());
        toughJetRequest.setFrom(busyFlightsRequest.getOrigin());
        toughJetRequest.setInboundDate(busyFlightsRequest.getReturnDate());
        toughJetRequest.setNumberOfAdults(busyFlightsRequest.getNumberOfPassengers());
        return toughJetRequest;
    }


}
