package com.travix.medusa.busyflights.resources;

import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.repository.ToughJetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/supplier/service/toughjet")
public class ToughJetResource {

    @Autowired
    private ToughJetRepository toughJetRepository;

    @GetMapping("/getToughJetFlights")
    public List<ToughJetResponse> fetchToughJetFlights(@RequestMapping final ToughJetRequest toughJetRequest){
        List<ToughJetResponse> toughJetResponses = new ArrayList<ToughJetResponse>();

        return toughJetResponses;
    }
}
