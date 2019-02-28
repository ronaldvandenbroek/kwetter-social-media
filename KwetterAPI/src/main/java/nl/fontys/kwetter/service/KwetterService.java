package nl.fontys.kwetter.service;

import nl.fontys.kwetter.dao.memory.IUserDao;
import nl.fontys.kwetter.dao.memory.IKwetterDao;
import nl.fontys.kwetter.exceptions.InvalidModelException;
import nl.fontys.kwetter.exceptions.KwetterDoesntExist;
import nl.fontys.kwetter.exceptions.UserDoesntExist;
import nl.fontys.kwetter.models.Kwetter;
import nl.fontys.kwetter.models.User;
import nl.fontys.kwetter.service.interfaces.IKwetterService;
import nl.fontys.kwetter.utilities.ModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Service for handling model operations regarding the kwetter tasks.
 */
@Service
public class KwetterService implements IKwetterService {

    private final ModelValidator validator;
    private final IUserDao userDao;
    private final IKwetterDao kwetterDao;

    private Calendar calendar;

    @Autowired
    public KwetterService(ModelValidator validator, IUserDao userDao, IKwetterDao kwetterDao) {
        calendar = Calendar.getInstance();

        this.validator = validator;
        this.userDao = userDao;
        this.kwetterDao = kwetterDao;
    }

    /**
     * Search for a specific kwetter via the text.
     *
     * @param searchTerm The text to search for.
     * @return The corresponding Kwetter
     */
    @Override
    public Kwetter searchForKwetter(String searchTerm) {
        throw new NotImplementedException();
    }

    /**
     * Create a new kwetter
     *
     * @param userId  Id of the User
     * @param kwetter The kwetter to be created
     * @return The created kwetter
     * @throws InvalidModelException Thrown when an invalid input is given for the model.
     * @throws UserDoesntExist       Thrown when the userID does not have a corresponding user.
     */
    @Override
    public Kwetter createKwetter(Long userId, Kwetter kwetter) throws UserDoesntExist, InvalidModelException {
        User owner = getUserById(userId);

        Set<User> mentions = new HashSet<>();
        if (kwetter.getMentions() != null) {
            for (User kwetterMentions : kwetter.getMentions()) {
                mentions.add(getUserById(kwetterMentions.getId()));
            }
        }

        kwetter.setMentions(mentions);
        kwetter.setDateTime(calendar.getTime());
        owner.addCreatedKwetter(kwetter);

        validator.validate(kwetter);

        kwetterDao.createNewKwetter(kwetter);
        userDao.updateUser(owner);
        return kwetter;
    }

    /**
     * Remove a kwetter
     *
     * @param userId    Id of the User
     * @param kwetterId Id of the Kwetter
     * @throws KwetterDoesntExist Thrown when the kwetterID does not have a corresponding Kwetter.
     * @throws UserDoesntExist    Thrown when the userID does not have a corresponding User.
     */
    @Override
    public void removeKwetter(Long userId, Long kwetterId) throws KwetterDoesntExist, UserDoesntExist {
        Kwetter kwetter = getKwetterById(kwetterId);
        User owner = getUserById(kwetter.getOwner().getId());

        if (kwetter.getOwner().getId().equals(userId)) {
            owner.removeCreatedKwetter(kwetter);
            userDao.updateUser(owner);
            kwetterDao.updateKwetter(kwetter);
        } else {
            throw new KwetterDoesntExist();
        }
    }

    /**
     * Heart a kwetter
     *
     * @param userId    Id of the User
     * @param kwetterId Id of the Kwetter
     * @throws KwetterDoesntExist Thrown when the kwetterID does not have a corresponding Kwetter.
     * @throws UserDoesntExist    Thrown when the userID does not have a corresponding User.
     */
    @Override
    public void heartKwetter(Long userId, Long kwetterId) throws KwetterDoesntExist, UserDoesntExist {
        Kwetter kwetter = getKwetterById(kwetterId);
        User user = getUserById(userId);

        user.addHeartedKwetter(kwetter);

        userDao.updateUser(user);
        kwetterDao.updateKwetter(kwetter);
    }

    /**
     * Remove a heart from a Kwetter
     *
     * @param userId    Id of the User
     * @param kwetterId Id of the Kwetter
     * @throws KwetterDoesntExist Thrown when the kwetterID does not have a corresponding Kwetter.
     * @throws UserDoesntExist    Thrown when the userID does not have a corresponding User.
     */
    @Override
    public void removeHeartKwetter(Long userId, Long kwetterId) throws KwetterDoesntExist, UserDoesntExist {
        Kwetter kwetter = getKwetterById(kwetterId);
        User user = getUserById(userId);

        user.removeHeartedKwetter(kwetter);

        userDao.updateUser(user);
        kwetterDao.updateKwetter(kwetter);
    }

    /**
     * Report a Kwetter
     *
     * @param userId    Id of the User
     * @param kwetterId Id of the Kwetter
     * @throws KwetterDoesntExist Thrown when the kwetterID does not have a corresponding Kwetter.
     * @throws UserDoesntExist    Thrown when the userID does not have a corresponding User.
     */
    @Override
    public void reportKwetter(Long userId, Long kwetterId) throws KwetterDoesntExist, UserDoesntExist {
        Kwetter kwetter = getKwetterById(kwetterId);
        User user = getUserById(userId);

        user.addReportedKwetter(kwetter);

        userDao.updateUser(user);
        kwetterDao.updateKwetter(kwetter);
    }

    /**
     * Remove a report from a Kwetter
     *
     * @param userId    Id of the User
     * @param kwetterId Id of the Kwetter
     * @throws KwetterDoesntExist Thrown when the kwetterID does not have a corresponding Kwetter.
     * @throws UserDoesntExist    Thrown when the userID does not have a corresponding User.
     */
    @Override
    public void removeReportKwetter(Long userId, Long kwetterId) throws KwetterDoesntExist, UserDoesntExist {
        Kwetter kwetter = getKwetterById(kwetterId);
        User user = getUserById(userId);

        user.removeReportedKwetter(kwetter);

        userDao.updateUser(user);
        kwetterDao.updateKwetter(kwetter);
    }

    /**
     * Get all Kwetters the user is mentioned in.
     *
     * @param userId Id of the User
     * @return List of all Kwetters the user is mentioned in.
     * @throws UserDoesntExist Thrown when the userID does not have a corresponding User.
     */
    @Override
    public List<Kwetter> getMentionedKwetters(Long userId) throws UserDoesntExist {
        User user = getUserById(userId);
        throw new NotImplementedException();
    }

    /**
     * Get the most recent Kwetters of a User
     *
     * @param userId Id of the User
     * @return List of the most recent Kwetters
     * @throws UserDoesntExist Thrown when the userID does not have a corresponding User.
     */
    @Override
    public List<Kwetter> getMostRecentKwetters(Long userId) throws UserDoesntExist {
        User user = getUserById(userId);

        List<Kwetter> a = new ArrayList<>(user.getCreatedKwetters());
        ListIterator<Kwetter> li = a.listIterator(a.size());
        List<Kwetter> lastKwetters = new ArrayList<>();

        int i = 0;
        while (li.hasPrevious() || i < 10) {
            lastKwetters.add(li.previous());
            i++;
        }
        return lastKwetters;
    }

    /**
     * Get a list of the Kwetter a user hearted.
     *
     * @param userId Id of the User
     * @return List of the hearted kwetters
     * @throws UserDoesntExist Thrown when the userID does not have a corresponding User.
     */
    @Override
    public List<Kwetter> getHeartedKwetters(Long userId) throws UserDoesntExist {
        User user = getUserById(userId);
        return new ArrayList<>(user.getHeartedKwetters());
    }

    /**
     * Get the user via its Id
     *
     * @param userID Id of the User
     * @return The User
     * @throws UserDoesntExist Thrown when the userID does not have a corresponding user.
     */
    private User getUserById(Long userID) throws UserDoesntExist {
        User user = userDao.getUserById(userID);
        if (user == null) {
            throw new UserDoesntExist();
        }
        return user;
    }

    /**
     * Get the Kwetter via its Id
     *
     * @param kwetterId Id of the User
     * @return The Kwetter
     * @throws KwetterDoesntExist Thrown when the kwetterID does not have a corresponding Kwetter.
     */
    private Kwetter getKwetterById(Long kwetterId) throws KwetterDoesntExist {
        Kwetter kwetter = kwetterDao.getKwetterById(kwetterId);
        if (kwetter == null) {
            throw new KwetterDoesntExist();
        }
        return kwetter;
    }
}