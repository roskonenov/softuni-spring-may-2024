package bg.softuni.mobilele.offers.web;

import bg.softuni.mobilele.offers.model.dto.AddOfferDTO;
import bg.softuni.mobilele.offers.model.dto.ErrorDTO;
import bg.softuni.mobilele.offers.model.dto.OfferDTO;
import bg.softuni.mobilele.offers.service.OfferService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/offers")
public class OfferController {

  private static final Logger LOGGER = LoggerFactory.getLogger(OfferController.class);
  private final OfferService offerService;

  public OfferController(OfferService offerService) {
    this.offerService = offerService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<OfferDTO> findById(@PathVariable("id") Long id) {
    return ResponseEntity
        .ok(offerService.getOfferById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<OfferDTO> deleteById(@PathVariable("id") Long id) {
    offerService.deleteOffer(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<PagedModel<OfferDTO>> getAllOffers(@PageableDefault(
      size = 3,
      sort = "id",
      direction = Direction.ASC
  ) Pageable pageable) {
    return ResponseEntity.ok(
        new PagedModel<>(offerService.getAllOffers(pageable))
    );
  }

  @PostMapping
  public ResponseEntity<OfferDTO> createOffer(
      @RequestBody AddOfferDTO addOfferDTO,
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    LOGGER.info("Going to create an offer {}", addOfferDTO);

    OfferDTO offerDTO = offerService.createOffer(addOfferDTO, userDetails.getUsername());
    return ResponseEntity.created(
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(offerDTO.id())
            .toUri()
    ).body(offerDTO);
  }

}
