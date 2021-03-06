package com.github.phillipkruger.user.graphql;

import com.github.phillipkruger.user.model.Person;
import com.github.phillipkruger.user.model.Score;
import com.github.phillipkruger.user.service.PersonService;
import com.github.phillipkruger.user.service.ScoreService;
import java.util.List;
import java.util.Random;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

@GraphQLApi
public class PersonGraphQLApi {
    
    @Inject 
    ScoreService scoreService;
    
    @Inject
    PersonService personService;
    
    @Query
    @Timed(name = "personTimer", description = "How long does it take to get a Person.", unit = MetricUnits.NANOSECONDS)
    @Counted(name = "personCount", description = "How many times did we ask for Person.")
    public Person getPerson(Long id){
        return personService.getPerson(id);
    }
    
    @Query
    public List<Person> getPeople(){
        return personService.getPeople();
    }
    
    //@RolesAllowed("admin")
    public List<Score> getScores(@Source Person p) throws ScoresNotAvailableException{
        return scoreService.getScores(p.getIdNumber());
        //throw new ScoresNotAvailableException("Scores for person [" + p.getId() + "] not avaialble");
    }
    
    @Query
    public Integer getRandomNumber(@Min(10) long seed){
        Random random = new Random(seed);
        return random.nextInt();
    }
    
    // Mutations
    
    @Mutation
    public Person updatePerson(Person person){
        return personService.updateOrCreate(person);
    }
    
    @Mutation
    public Person deletePerson(Long id){
        return personService.delete(id);
    }
    
    // Default values
    @Query
    public List<Person> getPersonsWithSurname(
            @DefaultValue("Doyle") String surname) {
        return personService.getPeopleWithSurname(surname);
    }
    
}
