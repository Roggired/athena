#!/usr/bin/env bash

CA_PRIVATE_KEY='CA-private-key.key'
CA_SUBJECT='/CN=Certificate authority/'
CA_CERTIFICATE_SIGNING_REQUEST='CA-certificate-signing-request.csr'
CA_SELF_SIGNED_CERTIFICATE='CA-self-signed-certificate.pem'
SERVER_PRIVATE_KEY='Server-private-key.key'
SERVER_SUBJECT='/CN=yofik.messenger.auth/'
SERVER_CERTIFICATE_SIGNING_REQUEST='Server-certificate-signing-request.csr'
SERVER_CERTIFICATE='Server-certificate.pem'
SERVER_KEYSTORE='Server-keystore.p12'
SERVER_TRUSTSTORE='Server-truststore.p12'
CLIENT_PRIVATE_KEY='Client-private-key.key'
CLIENT_SUBJECT='/CN=Postman_Local/'
CLIENT_CERTIFICATE_SIGNING_REQUEST='Client-certificate-signing-request.csr'
CLIENT_CERTIFICATE='Client-certificate.pem'
DAYS=365
PASSWORD='password'

print_done() {
        echo "...done! $1"
        echo ''
}

private_key() {
        openssl genrsa -aes256 -passout pass:$PASSWORD -out $1 4096 2>/dev/null
}

certificate_signing_request() {
        openssl req -new -key $1 -passin pass:$PASSWORD -subj "$2" -out $3 >/dev/null
}

sign_request() {
        openssl x509 -req -in $1 -CA $2 -CAkey $3 -passin pass:$PASSWORD -CAcreateserial -days $DAYS -out $4 2>/dev/null
}

echo 'Remove any key, pem, csr, p12 files'
rm -f *.key *.pem *.csr *.p12
print_done

echo 'Create private key for CA'
private_key $CA_PRIVATE_KEY
print_done $CA_PRIVATE_KEY

echo 'Create certificate signing request for CA'
certificate_signing_request $CA_PRIVATE_KEY "$CA_SUBJECT" $CA_CERTIFICATE_SIGNING_REQUEST
print_done $CA_CERTIFICATE_SIGNING_REQUEST

echo 'Create self signed certificate for CA'
openssl x509 -req -in $CA_CERTIFICATE_SIGNING_REQUEST -signkey $CA_PRIVATE_KEY -passin pass:$PASSWORD -days $DAYS -out $CA_SELF_SIGNED_CERTIFICATE 2>/dev/null
print_done $CA_SELF_SIGNED_CERTIFICATE

echo 'Create private key for Server'
private_key $SERVER_PRIVATE_KEY
print_done $SERVER_PRIVATE_KEY

echo 'Create certificate signing request for Server'
certificate_signing_request $SERVER_PRIVATE_KEY "$SERVER_SUBJECT" $SERVER_CERTIFICATE_SIGNING_REQUEST
print_done $SERVER_CERTIFICATE_SIGNING_REQUEST

echo 'Sign Server'\''s certificate signing request with CA'\''s self signed certificate'
sign_request $SERVER_CERTIFICATE_SIGNING_REQUEST $CA_SELF_SIGNED_CERTIFICATE $CA_PRIVATE_KEY $SERVER_CERTIFICATE
print_done $SERVER_CERTIFICATE

echo 'Create private key for Client'
private_key $CLIENT_PRIVATE_KEY
print_done $CLIENT_PRIVATE_KEY

echo 'Create certificate signing request for Client'
certificate_signing_request $CLIENT_PRIVATE_KEY "$CLIENT_SUBJECT" $CLIENT_CERTIFICATE_SIGNING_REQUEST
print_done $CLIENT_CERTIFICATE_SIGNING_REQUEST

echo 'Sign Client'\''s certificate signing request with CA'\''s self signed certificate'
sign_request $CLIENT_CERTIFICATE_SIGNING_REQUEST $CA_SELF_SIGNED_CERTIFICATE $CA_PRIVATE_KEY $CLIENT_CERTIFICATE
print_done $CLIENT_CERTIFICATE

echo 'Create PKCS12 keystore for Server'
openssl pkcs12 -export -in $SERVER_CERTIFICATE -inkey $SERVER_PRIVATE_KEY -passin pass:$PASSWORD -passout pass:$PASSWORD -out $SERVER_KEYSTORE >/dev/null
print_done $SERVER_KEYSTORE

echo 'Create PKCS12 truststore for Server'
keytool -import -file $CA_SELF_SIGNED_CERTIFICATE -keystore $SERVER_TRUSTSTORE -storetype PKCS12 -storepass $PASSWORD -noprompt 2>/dev/null
print_done $SERVER_TRUSTSTORE
