package se331.lab.rest.security.token;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    final TokenDao tokenDao;

    @Override
    @Transactional
    public void save(Token token) {
        tokenDao.save(token);
    }
}
