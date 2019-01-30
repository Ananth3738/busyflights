package com.travix.medusa.busyflights.resources;

import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.repository.ToughJetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/supplier/service/toughjet")
public class ToughJetResource {

    @Autowired
    private ToughJetRepository toughJetRepository;

    @GetMapping("/getToughJetFlights")
    public List<ToughJetResponse> fetchToughJetFlights(@RequestBody final ToughJetRequest toughJetRequest) {
        List<ToughJetResponse> toughJetResponses = new ArrayList<ToughJetResponse>();
        // fecth the details from repository
        toughJetResponses = toughJetResponses.stream()
                .sorted(Comparator.comparing(ToughJetResponse::getBasePrice))
                .collect(Collectors.toList());
        return toughJetResponses;
    }
}
