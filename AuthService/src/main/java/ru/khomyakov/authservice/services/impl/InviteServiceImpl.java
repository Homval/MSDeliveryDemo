package ru.khomyakov.authservice.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import ru.khomyakov.authservice.models.dto.RestoreMessage;
import ru.khomyakov.authservice.models.dto.RestoreTokenMessage;
import ru.khomyakov.authservice.models.entities.Invite;
import ru.khomyakov.authservice.models.entities.User;
import ru.khomyakov.authservice.models.enums.StatusType;
import ru.khomyakov.authservice.repositories.InviteRepository;
import ru.khomyakov.authservice.services.InviteService;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class InviteServiceImpl implements InviteService {

    private final ValueOperations<String, String> inviteOps;
    private final String invitePrefix;
    private final long EXPIRATION_TOKEN_TIME;
    private final long EXPIRATION_INVITE_TIME;
    private final InviteRepository inviteRepository;

    public InviteServiceImpl(ValueOperations<String, String> inviteOps,
                             InviteRepository inviteRepository,
                             @Value("${invite.invite.expiration}") long expirationInviteTime,
                             @Value("${invite.token.expiration}") long expirationTime,
                             @Value("${redis.cache.prefix.response-status}") String invitePrefix) {
        this.inviteOps = inviteOps;
        this.inviteRepository = inviteRepository;
        this.EXPIRATION_TOKEN_TIME = expirationTime;
        this.EXPIRATION_INVITE_TIME = expirationInviteTime;
        this.invitePrefix = invitePrefix;
    }


    @Override
    public RestoreTokenMessage startProcessPasswordRestore(User user) {
        Invite invite = createRestoreInvite(user);
        StatusType status = StatusType.NEW;
        inviteOps.set(invitePrefix + invite.getToken(), status.toString(), EXPIRATION_INVITE_TIME, TimeUnit.HOURS);
        return RestoreTokenMessage.builder()
                .responseUuid(UUID.randomUUID())
                .token(invite.getToken())
                .statusType(status)
                .build();
    }

    private Invite createRestoreInvite(User user) {
        String token = generateToken();
        Invite invite = new Invite();
        invite.setUserId(user.getId());
        invite.setToken(token);
        invite.setExpirationDateTime(LocalDateTime.now().plusSeconds(EXPIRATION_TOKEN_TIME));
        return inviteRepository.save(invite);
    }

    private String generateToken() {
        Random random = new Random();
        String alpha = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            token.append(alpha.charAt(random.nextInt(alpha.length())));
        }
        return token.toString();
    }
}
