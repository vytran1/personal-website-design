package com.personalinformation.vta.features.user.deleteUser;


import com.personalinformation.vta.common.ICommand;
import com.personalinformation.vta.common.ICommandVoid;
import com.personalinformation.vta.common.exception.UserNotFoundException;
import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.candidate.CandidateRepository;
import com.personalinformation.vta.features.user.UserRepository;
import io.membrane_api.jmediator.Handler;

import java.util.Optional;

record DeleteUserCommand(String email)  implements ICommandVoid {};

@Handler
public class DeleteUserHandler {


    private final UserRepository userRepository;

    private final CandidateRepository candidateRepository;


    public DeleteUserHandler(UserRepository userRepository, CandidateRepository candidateRepository) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
    }


    public void deleteUser(DeleteUserCommand command) throws UserNotFoundException {

        String email = command.email();

        //Check user is exist

        Optional<User> userOPT = userRepository.findByEmail(email);

        if(!userOPT.isPresent()){
            throw new UserNotFoundException("Not exist user with the given email");
        }

        userRepository.delete(userOPT.get());

        Optional<Candidate> candidateOPT = candidateRepository.findCandidateByEmail(email);

        if(candidateOPT.isPresent()){
           candidateRepository.delete(candidateOPT.get());
        }
    }

}
