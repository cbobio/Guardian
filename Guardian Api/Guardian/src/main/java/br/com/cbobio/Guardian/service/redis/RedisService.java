

package br.com.cbobio.Guardian.service.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    public boolean existe(String chave) {
        RBucket<String> bucket =
                redissonClient.getBucket(chave);

        return bucket.isExists();
    }

    public void salvar(
            String chave,
            String valor,
            Duration expiracao) {

        RBucket<String> bucket =
                redissonClient.getBucket(chave);

        bucket.set(valor, expiracao);
    }

    public String buscar(String chave) {

        RBucket<String> bucket =
                redissonClient.getBucket(chave);

        return bucket.get();
    }

    public void remover(String chave) {

        RBucket<String> bucket =
                redissonClient.getBucket(chave);

        bucket.delete();
    }

    public void removerPorPrefixo(String prefixo) {
        redissonClient.getKeys()
                .deleteByPattern(prefixo + "*");
        log.debug(
                "Cache Redis invalidado. Prefixo: {}",
                prefixo
        );
    }

    public void salvarObjeto(
            String chave,
            Object objeto,
            Duration expiracao) {

        try {
            String json = objectMapper.writeValueAsString(objeto);
            salvar(chave, json, expiracao);
        } catch (JacksonException e) {
            throw new IllegalStateException(
                    "Erro ao converter objeto para JSON no Redis.", e);
        }
    }

    public <T> T buscarObjeto(
            String chave,
            Class<T> tipo) {

        String json = buscar(chave);

        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, tipo);
        } catch (JacksonException e) {
            throw new IllegalStateException(
                    "Erro ao converter JSON do Redis para objeto.", e);
        }
    }
}