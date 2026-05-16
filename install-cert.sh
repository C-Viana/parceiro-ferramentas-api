#!/bin/bash
# Remove se o certificado já existir para evitar duplicidade
keytool -delete -alias simur-local-cert -cacerts -storepass changeit -noprompt 2>/dev/null || true
# Importa o certificado atualizado
keytool -importcert -file /tmp/simur.crt -alias simur-local-cert -cacerts -storepass changeit -noprompt
# Inicia a aplicação Java
exec java -jar ../app.jar