package nl.fontys.kwetter.service.implementation;

import nl.fontys.kwetter.controllers.AdminController;
import nl.fontys.kwetter.controllers.KwetterController;
import nl.fontys.kwetter.controllers.UserController;
import nl.fontys.kwetter.exceptions.FailedToAddLinksException;
import nl.fontys.kwetter.exceptions.ModelInvalidException;
import nl.fontys.kwetter.exceptions.ModelNotFoundException;
import nl.fontys.kwetter.models.dto.KwetterDTO;
import nl.fontys.kwetter.models.dto.UserDTO;
import nl.fontys.kwetter.models.entity.Kwetter;
import nl.fontys.kwetter.models.entity.User;
import nl.fontys.kwetter.service.IHateoasService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class HateoasService implements IHateoasService {

    public UserDTO getUserDTOWithLinks(User user) {
        UserDTO userDTO = new UserDTO(user);
        userDTO.add(getUserLinks(user.getId()));
        return userDTO;
    }

    public List<UserDTO> getUserDTOWithLinks(List<User> users) {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(getUserDTOWithLinks(user));
        }
        return userDTOs;
    }

    @Override
    public KwetterDTO getKwetterDTOWithLinks(Kwetter kwetter) {
        KwetterDTO kwetterDTO = new KwetterDTO(kwetter);
        kwetterDTO.add(getKwetterLinks(kwetter.getUuid()));
        return kwetterDTO;
    }

    @Override
    public List<KwetterDTO> getKwetterDTOWithLinks(List<Kwetter> kwetters) {
        List<KwetterDTO> kwetterDTOs = new ArrayList<>();
        for (Kwetter kwetter : kwetters) {
            kwetterDTOs.add(getKwetterDTOWithLinks(kwetter));
        }
        return kwetterDTOs;
    }

    private List<Link> getUserLinks(UUID id) {
        try {
            List<Link> links = new ArrayList<>();
            links.add(linkTo(methodOn(UserController.class).getProfile(id)).withSelfRel());
            links.add(linkTo(methodOn(UserController.class).getFollowers(id)).withRel("followers"));
            links.add(linkTo(methodOn(UserController.class).getFollowing(id)).withRel("following"));
            links.add(linkTo(methodOn(UserController.class).getProfile(id)).withRel("profile"));
            links.add(linkTo(methodOn(UserController.class).follow(id, null)).withRel("follow"));
            links.add(linkTo(methodOn(UserController.class).unfollow(id, null)).withRel("unfollow"));
            links.add(linkTo(methodOn(KwetterController.class).getHeartedKwetters(id)).withRel("heartedKwetters"));
            links.add(linkTo(methodOn(KwetterController.class).getMostRecentKwetters(id)).withRel("mostRecentKwetters"));
            links.add(linkTo(methodOn(KwetterController.class).getTimeline(id)).withRel("timeline"));
            links.add(linkTo(methodOn(KwetterController.class).searchForKwetter(null)).withRel("search"));
            links.add(linkTo(methodOn(KwetterController.class).createKwetter(id, null)).withRel("create"));
            links.add(linkTo(methodOn(AdminController.class).getAllUsers()).withRel("users"));
            links.add(linkTo(methodOn(AdminController.class).getAllUsers()).withRel("kwetters"));
            return links;
        } catch (ModelNotFoundException | ModelInvalidException e) {
            throw new FailedToAddLinksException(e.getMessage());
        }
    }

    private List<Link> getKwetterLinks(UUID id) {
        try {
            List<Link> links = new ArrayList<>();
            links.add(linkTo(methodOn(UserController.class).getProfile(id)).withRel("owner"));
            return links;
        } catch (ModelNotFoundException e) {
            throw new FailedToAddLinksException(e.getMessage());
        }
    }
}
