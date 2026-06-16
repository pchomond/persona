# Persona Service

## Overview

This application acts as a **Downstream Identity Companion**. It serves as the authoritative middle-man between any application (Frontends / Microservices) and Keycloak.

It provides the ability to handle, store and retrieve user (non-sensitive) data in an application-specific schema. It is **NOT** a Keycloak (or any IdP's) facade regarding the actual user registration.

Furthermore, this service extends standard user identities with rich and relational profile data that is too complex for standard IdP `app_metadata` fields.

While this is currently being developed with Keycloak integration in mind, it is intended to grow into a vendor-agnostic solution.

---