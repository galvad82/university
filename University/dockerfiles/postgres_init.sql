CREATE USER keycloak_user WITH PASSWORD 'keycloak' CREATEDB;
CREATE DATABASE keycloak_db
    WITH 
    OWNER = keycloak_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

\connect keycloak_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 241 (class 1259 OID 17046)
-- Name: admin_event_entity; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.admin_event_entity (
    id character varying(36) NOT NULL,
    admin_event_time bigint,
    realm_id character varying(255),
    operation_type character varying(255),
    auth_realm_id character varying(255),
    auth_client_id character varying(255),
    auth_user_id character varying(255),
    ip_address character varying(255),
    resource_path character varying(2550),
    representation text,
    error character varying(255),
    resource_type character varying(64)
);


ALTER TABLE public.admin_event_entity OWNER TO keycloak_user;

--
-- TOC entry 270 (class 1259 OID 17509)
-- Name: associated_policy; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.associated_policy (
    policy_id character varying(36) NOT NULL,
    associated_policy_id character varying(36) NOT NULL
);


ALTER TABLE public.associated_policy OWNER TO keycloak_user;

--
-- TOC entry 244 (class 1259 OID 17064)
-- Name: authentication_execution; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.authentication_execution (
    id character varying(36) NOT NULL,
    alias character varying(255),
    authenticator character varying(36),
    realm_id character varying(36),
    flow_id character varying(36),
    requirement integer,
    priority integer,
    authenticator_flow boolean DEFAULT false NOT NULL,
    auth_flow_id character varying(36),
    auth_config character varying(36)
);


ALTER TABLE public.authentication_execution OWNER TO keycloak_user;

--
-- TOC entry 243 (class 1259 OID 17058)
-- Name: authentication_flow; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.authentication_flow (
    id character varying(36) NOT NULL,
    alias character varying(255),
    description character varying(255),
    realm_id character varying(36),
    provider_id character varying(36) DEFAULT 'basic-flow'::character varying NOT NULL,
    top_level boolean DEFAULT false NOT NULL,
    built_in boolean DEFAULT false NOT NULL
);


ALTER TABLE public.authentication_flow OWNER TO keycloak_user;

--
-- TOC entry 242 (class 1259 OID 17052)
-- Name: authenticator_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.authenticator_config (
    id character varying(36) NOT NULL,
    alias character varying(255),
    realm_id character varying(36)
);


ALTER TABLE public.authenticator_config OWNER TO keycloak_user;

--
-- TOC entry 245 (class 1259 OID 17069)
-- Name: authenticator_config_entry; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.authenticator_config_entry (
    authenticator_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.authenticator_config_entry OWNER TO keycloak_user;

--
-- TOC entry 271 (class 1259 OID 17524)
-- Name: broker_link; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.broker_link (
    identity_provider character varying(255) NOT NULL,
    storage_provider_id character varying(255),
    realm_id character varying(36) NOT NULL,
    broker_user_id character varying(255),
    broker_username character varying(255),
    token text,
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.broker_link OWNER TO keycloak_user;

--
-- TOC entry 202 (class 1259 OID 16401)
-- Name: client; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client (
    id character varying(36) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    full_scope_allowed boolean DEFAULT false NOT NULL,
    client_id character varying(255),
    not_before integer,
    public_client boolean DEFAULT false NOT NULL,
    secret character varying(255),
    base_url character varying(255),
    bearer_only boolean DEFAULT false NOT NULL,
    management_url character varying(255),
    surrogate_auth_required boolean DEFAULT false NOT NULL,
    realm_id character varying(36),
    protocol character varying(255),
    node_rereg_timeout integer DEFAULT 0,
    frontchannel_logout boolean DEFAULT false NOT NULL,
    consent_required boolean DEFAULT false NOT NULL,
    name character varying(255),
    service_accounts_enabled boolean DEFAULT false NOT NULL,
    client_authenticator_type character varying(255),
    root_url character varying(255),
    description character varying(255),
    registration_token character varying(255),
    standard_flow_enabled boolean DEFAULT true NOT NULL,
    implicit_flow_enabled boolean DEFAULT false NOT NULL,
    direct_access_grants_enabled boolean DEFAULT false NOT NULL,
    always_display_in_console boolean DEFAULT false NOT NULL
);


ALTER TABLE public.client OWNER TO keycloak_user;

--
-- TOC entry 225 (class 1259 OID 16775)
-- Name: client_attributes; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_attributes (
    client_id character varying(36) NOT NULL,
    value character varying(4000),
    name character varying(255) NOT NULL
);


ALTER TABLE public.client_attributes OWNER TO keycloak_user;

--
-- TOC entry 282 (class 1259 OID 17783)
-- Name: client_auth_flow_bindings; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_auth_flow_bindings (
    client_id character varying(36) NOT NULL,
    flow_id character varying(36),
    binding_name character varying(255) NOT NULL
);


ALTER TABLE public.client_auth_flow_bindings OWNER TO keycloak_user;

--
-- TOC entry 281 (class 1259 OID 17658)
-- Name: client_initial_access; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_initial_access (
    id character varying(36) NOT NULL,
    realm_id character varying(36) NOT NULL,
    "timestamp" integer,
    expiration integer,
    count integer,
    remaining_count integer
);


ALTER TABLE public.client_initial_access OWNER TO keycloak_user;

--
-- TOC entry 227 (class 1259 OID 16787)
-- Name: client_node_registrations; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_node_registrations (
    client_id character varying(36) NOT NULL,
    value integer,
    name character varying(255) NOT NULL
);


ALTER TABLE public.client_node_registrations OWNER TO keycloak_user;

--
-- TOC entry 259 (class 1259 OID 17307)
-- Name: client_scope; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_scope (
    id character varying(36) NOT NULL,
    name character varying(255),
    realm_id character varying(36),
    description character varying(255),
    protocol character varying(255)
);


ALTER TABLE public.client_scope OWNER TO keycloak_user;

--
-- TOC entry 260 (class 1259 OID 17322)
-- Name: client_scope_attributes; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_scope_attributes (
    scope_id character varying(36) NOT NULL,
    value character varying(2048),
    name character varying(255) NOT NULL
);


ALTER TABLE public.client_scope_attributes OWNER TO keycloak_user;

--
-- TOC entry 283 (class 1259 OID 17825)
-- Name: client_scope_client; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_scope_client (
    client_id character varying(255) NOT NULL,
    scope_id character varying(255) NOT NULL,
    default_scope boolean DEFAULT false NOT NULL
);


ALTER TABLE public.client_scope_client OWNER TO keycloak_user;

--
-- TOC entry 261 (class 1259 OID 17328)
-- Name: client_scope_role_mapping; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_scope_role_mapping (
    scope_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE public.client_scope_role_mapping OWNER TO keycloak_user;

--
-- TOC entry 203 (class 1259 OID 16413)
-- Name: client_session; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_session (
    id character varying(36) NOT NULL,
    client_id character varying(36),
    redirect_uri character varying(255),
    state character varying(255),
    "timestamp" integer,
    session_id character varying(36),
    auth_method character varying(255),
    realm_id character varying(255),
    auth_user_id character varying(36),
    current_action character varying(36)
);


ALTER TABLE public.client_session OWNER TO keycloak_user;

--
-- TOC entry 248 (class 1259 OID 17090)
-- Name: client_session_auth_status; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_session_auth_status (
    authenticator character varying(36) NOT NULL,
    status integer,
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_auth_status OWNER TO keycloak_user;

--
-- TOC entry 226 (class 1259 OID 16781)
-- Name: client_session_note; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_session_note (
    name character varying(255) NOT NULL,
    value character varying(255),
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_note OWNER TO keycloak_user;

--
-- TOC entry 240 (class 1259 OID 16968)
-- Name: client_session_prot_mapper; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_session_prot_mapper (
    protocol_mapper_id character varying(36) NOT NULL,
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_prot_mapper OWNER TO keycloak_user;

--
-- TOC entry 204 (class 1259 OID 16419)
-- Name: client_session_role; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_session_role (
    role_id character varying(255) NOT NULL,
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_role OWNER TO keycloak_user;

--
-- TOC entry 249 (class 1259 OID 17171)
-- Name: client_user_session_note; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.client_user_session_note (
    name character varying(255) NOT NULL,
    value character varying(2048),
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_user_session_note OWNER TO keycloak_user;

--
-- TOC entry 279 (class 1259 OID 17574)
-- Name: component; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.component (
    id character varying(36) NOT NULL,
    name character varying(255),
    parent_id character varying(36),
    provider_id character varying(36),
    provider_type character varying(255),
    realm_id character varying(36),
    sub_type character varying(255)
);


ALTER TABLE public.component OWNER TO keycloak_user;

--
-- TOC entry 278 (class 1259 OID 17568)
-- Name: component_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.component_config (
    id character varying(36) NOT NULL,
    component_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(4000)
);


ALTER TABLE public.component_config OWNER TO keycloak_user;

--
-- TOC entry 205 (class 1259 OID 16422)
-- Name: composite_role; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.composite_role (
    composite character varying(36) NOT NULL,
    child_role character varying(36) NOT NULL
);


ALTER TABLE public.composite_role OWNER TO keycloak_user;

--
-- TOC entry 206 (class 1259 OID 16425)
-- Name: credential; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.credential (
    id character varying(36) NOT NULL,
    salt bytea,
    type character varying(255),
    user_id character varying(36),
    created_date bigint,
    user_label character varying(255),
    secret_data text,
    credential_data text,
    priority integer
);


ALTER TABLE public.credential OWNER TO keycloak_user;

--
-- TOC entry 201 (class 1259 OID 16392)
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO keycloak_user;

--
-- TOC entry 200 (class 1259 OID 16387)
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO keycloak_user;

--
-- TOC entry 284 (class 1259 OID 17841)
-- Name: default_client_scope; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.default_client_scope (
    realm_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL,
    default_scope boolean DEFAULT false NOT NULL
);


ALTER TABLE public.default_client_scope OWNER TO keycloak_user;

--
-- TOC entry 207 (class 1259 OID 16431)
-- Name: event_entity; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.event_entity (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    details_json character varying(2550),
    error character varying(255),
    ip_address character varying(255),
    realm_id character varying(255),
    session_id character varying(255),
    event_time bigint,
    type character varying(255),
    user_id character varying(255)
);


ALTER TABLE public.event_entity OWNER TO keycloak_user;

--
-- TOC entry 272 (class 1259 OID 17530)
-- Name: fed_user_attribute; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.fed_user_attribute (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    value character varying(2024)
);


ALTER TABLE public.fed_user_attribute OWNER TO keycloak_user;

--
-- TOC entry 273 (class 1259 OID 17536)
-- Name: fed_user_consent; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.fed_user_consent (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    created_date bigint,
    last_updated_date bigint,
    client_storage_provider character varying(36),
    external_client_id character varying(255)
);


ALTER TABLE public.fed_user_consent OWNER TO keycloak_user;

--
-- TOC entry 286 (class 1259 OID 17867)
-- Name: fed_user_consent_cl_scope; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.fed_user_consent_cl_scope (
    user_consent_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE public.fed_user_consent_cl_scope OWNER TO keycloak_user;

--
-- TOC entry 274 (class 1259 OID 17545)
-- Name: fed_user_credential; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.fed_user_credential (
    id character varying(36) NOT NULL,
    salt bytea,
    type character varying(255),
    created_date bigint,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    user_label character varying(255),
    secret_data text,
    credential_data text,
    priority integer
);


ALTER TABLE public.fed_user_credential OWNER TO keycloak_user;

--
-- TOC entry 275 (class 1259 OID 17555)
-- Name: fed_user_group_membership; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.fed_user_group_membership (
    group_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE public.fed_user_group_membership OWNER TO keycloak_user;

--
-- TOC entry 276 (class 1259 OID 17558)
-- Name: fed_user_required_action; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.fed_user_required_action (
    required_action character varying(255) DEFAULT ' '::character varying NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE public.fed_user_required_action OWNER TO keycloak_user;

--
-- TOC entry 277 (class 1259 OID 17565)
-- Name: fed_user_role_mapping; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.fed_user_role_mapping (
    role_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE public.fed_user_role_mapping OWNER TO keycloak_user;

--
-- TOC entry 230 (class 1259 OID 16825)
-- Name: federated_identity; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.federated_identity (
    identity_provider character varying(255) NOT NULL,
    realm_id character varying(36),
    federated_user_id character varying(255),
    federated_username character varying(255),
    token text,
    user_id character varying(36) NOT NULL
);


ALTER TABLE public.federated_identity OWNER TO keycloak_user;

--
-- TOC entry 280 (class 1259 OID 17634)
-- Name: federated_user; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.federated_user (
    id character varying(255) NOT NULL,
    storage_provider_id character varying(255),
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.federated_user OWNER TO keycloak_user;

--
-- TOC entry 256 (class 1259 OID 17244)
-- Name: group_attribute; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.group_attribute (
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    group_id character varying(36) NOT NULL
);


ALTER TABLE public.group_attribute OWNER TO keycloak_user;

--
-- TOC entry 255 (class 1259 OID 17241)
-- Name: group_role_mapping; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.group_role_mapping (
    role_id character varying(36) NOT NULL,
    group_id character varying(36) NOT NULL
);


ALTER TABLE public.group_role_mapping OWNER TO keycloak_user;

--
-- TOC entry 231 (class 1259 OID 16831)
-- Name: identity_provider; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.identity_provider (
    internal_id character varying(36) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    provider_alias character varying(255),
    provider_id character varying(255),
    store_token boolean DEFAULT false NOT NULL,
    authenticate_by_default boolean DEFAULT false NOT NULL,
    realm_id character varying(36),
    add_token_role boolean DEFAULT true NOT NULL,
    trust_email boolean DEFAULT false NOT NULL,
    first_broker_login_flow_id character varying(36),
    post_broker_login_flow_id character varying(36),
    provider_display_name character varying(255),
    link_only boolean DEFAULT false NOT NULL
);


ALTER TABLE public.identity_provider OWNER TO keycloak_user;

--
-- TOC entry 232 (class 1259 OID 16841)
-- Name: identity_provider_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.identity_provider_config (
    identity_provider_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.identity_provider_config OWNER TO keycloak_user;

--
-- TOC entry 237 (class 1259 OID 16947)
-- Name: identity_provider_mapper; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.identity_provider_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    idp_alias character varying(255) NOT NULL,
    idp_mapper_name character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.identity_provider_mapper OWNER TO keycloak_user;

--
-- TOC entry 238 (class 1259 OID 16953)
-- Name: idp_mapper_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.idp_mapper_config (
    idp_mapper_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.idp_mapper_config OWNER TO keycloak_user;

--
-- TOC entry 254 (class 1259 OID 17238)
-- Name: keycloak_group; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.keycloak_group (
    id character varying(36) NOT NULL,
    name character varying(255),
    parent_group character varying(36) NOT NULL,
    realm_id character varying(36)
);


ALTER TABLE public.keycloak_group OWNER TO keycloak_user;

--
-- TOC entry 208 (class 1259 OID 16440)
-- Name: keycloak_role; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.keycloak_role (
    id character varying(36) NOT NULL,
    client_realm_constraint character varying(255),
    client_role boolean DEFAULT false NOT NULL,
    description character varying(255),
    name character varying(255),
    realm_id character varying(255),
    client character varying(36),
    realm character varying(36)
);


ALTER TABLE public.keycloak_role OWNER TO keycloak_user;

--
-- TOC entry 236 (class 1259 OID 16944)
-- Name: migration_model; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.migration_model (
    id character varying(36) NOT NULL,
    version character varying(36),
    update_time bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.migration_model OWNER TO keycloak_user;

--
-- TOC entry 253 (class 1259 OID 17228)
-- Name: offline_client_session; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.offline_client_session (
    user_session_id character varying(36) NOT NULL,
    client_id character varying(255) NOT NULL,
    offline_flag character varying(4) NOT NULL,
    "timestamp" integer,
    data text,
    client_storage_provider character varying(36) DEFAULT 'local'::character varying NOT NULL,
    external_client_id character varying(255) DEFAULT 'local'::character varying NOT NULL
);


ALTER TABLE public.offline_client_session OWNER TO keycloak_user;

--
-- TOC entry 252 (class 1259 OID 17222)
-- Name: offline_user_session; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.offline_user_session (
    user_session_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    created_on integer NOT NULL,
    offline_flag character varying(4) NOT NULL,
    data text,
    last_session_refresh integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.offline_user_session OWNER TO keycloak_user;

--
-- TOC entry 266 (class 1259 OID 17451)
-- Name: policy_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.policy_config (
    policy_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value text
);


ALTER TABLE public.policy_config OWNER TO keycloak_user;

--
-- TOC entry 228 (class 1259 OID 16812)
-- Name: protocol_mapper; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.protocol_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    protocol character varying(255) NOT NULL,
    protocol_mapper_name character varying(255) NOT NULL,
    client_id character varying(36),
    client_scope_id character varying(36)
);


ALTER TABLE public.protocol_mapper OWNER TO keycloak_user;

--
-- TOC entry 229 (class 1259 OID 16819)
-- Name: protocol_mapper_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.protocol_mapper_config (
    protocol_mapper_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.protocol_mapper_config OWNER TO keycloak_user;

--
-- TOC entry 209 (class 1259 OID 16447)
-- Name: realm; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm (
    id character varying(36) NOT NULL,
    access_code_lifespan integer,
    user_action_lifespan integer,
    access_token_lifespan integer,
    account_theme character varying(255),
    admin_theme character varying(255),
    email_theme character varying(255),
    enabled boolean DEFAULT false NOT NULL,
    events_enabled boolean DEFAULT false NOT NULL,
    events_expiration bigint,
    login_theme character varying(255),
    name character varying(255),
    not_before integer,
    password_policy character varying(2550),
    registration_allowed boolean DEFAULT false NOT NULL,
    remember_me boolean DEFAULT false NOT NULL,
    reset_password_allowed boolean DEFAULT false NOT NULL,
    social boolean DEFAULT false NOT NULL,
    ssl_required character varying(255),
    sso_idle_timeout integer,
    sso_max_lifespan integer,
    update_profile_on_soc_login boolean DEFAULT false NOT NULL,
    verify_email boolean DEFAULT false NOT NULL,
    master_admin_client character varying(36),
    login_lifespan integer,
    internationalization_enabled boolean DEFAULT false NOT NULL,
    default_locale character varying(255),
    reg_email_as_username boolean DEFAULT false NOT NULL,
    admin_events_enabled boolean DEFAULT false NOT NULL,
    admin_events_details_enabled boolean DEFAULT false NOT NULL,
    edit_username_allowed boolean DEFAULT false NOT NULL,
    otp_policy_counter integer DEFAULT 0,
    otp_policy_window integer DEFAULT 1,
    otp_policy_period integer DEFAULT 30,
    otp_policy_digits integer DEFAULT 6,
    otp_policy_alg character varying(36) DEFAULT 'HmacSHA1'::character varying,
    otp_policy_type character varying(36) DEFAULT 'totp'::character varying,
    browser_flow character varying(36),
    registration_flow character varying(36),
    direct_grant_flow character varying(36),
    reset_credentials_flow character varying(36),
    client_auth_flow character varying(36),
    offline_session_idle_timeout integer DEFAULT 0,
    revoke_refresh_token boolean DEFAULT false NOT NULL,
    access_token_life_implicit integer DEFAULT 0,
    login_with_email_allowed boolean DEFAULT true NOT NULL,
    duplicate_emails_allowed boolean DEFAULT false NOT NULL,
    docker_auth_flow character varying(36),
    refresh_token_max_reuse integer DEFAULT 0,
    allow_user_managed_access boolean DEFAULT false NOT NULL,
    sso_max_lifespan_remember_me integer DEFAULT 0 NOT NULL,
    sso_idle_timeout_remember_me integer DEFAULT 0 NOT NULL,
    default_role character varying(255)
);


ALTER TABLE public.realm OWNER TO keycloak_user;

--
-- TOC entry 210 (class 1259 OID 16465)
-- Name: realm_attribute; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_attribute (
    name character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    value text
);


ALTER TABLE public.realm_attribute OWNER TO keycloak_user;

--
-- TOC entry 258 (class 1259 OID 17254)
-- Name: realm_default_groups; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_default_groups (
    realm_id character varying(36) NOT NULL,
    group_id character varying(36) NOT NULL
);


ALTER TABLE public.realm_default_groups OWNER TO keycloak_user;

--
-- TOC entry 235 (class 1259 OID 16936)
-- Name: realm_enabled_event_types; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_enabled_event_types (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.realm_enabled_event_types OWNER TO keycloak_user;

--
-- TOC entry 211 (class 1259 OID 16474)
-- Name: realm_events_listeners; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_events_listeners (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.realm_events_listeners OWNER TO keycloak_user;

--
-- TOC entry 291 (class 1259 OID 17981)
-- Name: realm_localizations; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_localizations (
    realm_id character varying(255) NOT NULL,
    locale character varying(255) NOT NULL,
    texts text NOT NULL
);


ALTER TABLE public.realm_localizations OWNER TO keycloak_user;

--
-- TOC entry 212 (class 1259 OID 16477)
-- Name: realm_required_credential; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_required_credential (
    type character varying(255) NOT NULL,
    form_label character varying(255),
    input boolean DEFAULT false NOT NULL,
    secret boolean DEFAULT false NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.realm_required_credential OWNER TO keycloak_user;

--
-- TOC entry 213 (class 1259 OID 16485)
-- Name: realm_smtp_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_smtp_config (
    realm_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.realm_smtp_config OWNER TO keycloak_user;

--
-- TOC entry 233 (class 1259 OID 16851)
-- Name: realm_supported_locales; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.realm_supported_locales (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.realm_supported_locales OWNER TO keycloak_user;

--
-- TOC entry 214 (class 1259 OID 16497)
-- Name: redirect_uris; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.redirect_uris (
    client_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.redirect_uris OWNER TO keycloak_user;

--
-- TOC entry 251 (class 1259 OID 17185)
-- Name: required_action_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.required_action_config (
    required_action_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.required_action_config OWNER TO keycloak_user;

--
-- TOC entry 250 (class 1259 OID 17177)
-- Name: required_action_provider; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.required_action_provider (
    id character varying(36) NOT NULL,
    alias character varying(255),
    name character varying(255),
    realm_id character varying(36),
    enabled boolean DEFAULT false NOT NULL,
    default_action boolean DEFAULT false NOT NULL,
    provider_id character varying(255),
    priority integer
);


ALTER TABLE public.required_action_provider OWNER TO keycloak_user;

--
-- TOC entry 288 (class 1259 OID 17906)
-- Name: resource_attribute; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_attribute (
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    resource_id character varying(36) NOT NULL
);


ALTER TABLE public.resource_attribute OWNER TO keycloak_user;

--
-- TOC entry 268 (class 1259 OID 17479)
-- Name: resource_policy; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_policy (
    resource_id character varying(36) NOT NULL,
    policy_id character varying(36) NOT NULL
);


ALTER TABLE public.resource_policy OWNER TO keycloak_user;

--
-- TOC entry 267 (class 1259 OID 17464)
-- Name: resource_scope; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_scope (
    resource_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE public.resource_scope OWNER TO keycloak_user;

--
-- TOC entry 262 (class 1259 OID 17398)
-- Name: resource_server; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_server (
    id character varying(36) NOT NULL,
    allow_rs_remote_mgmt boolean DEFAULT false NOT NULL,
    policy_enforce_mode character varying(15) NOT NULL,
    decision_strategy smallint DEFAULT 1 NOT NULL
);


ALTER TABLE public.resource_server OWNER TO keycloak_user;

--
-- TOC entry 287 (class 1259 OID 17882)
-- Name: resource_server_perm_ticket; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_server_perm_ticket (
    id character varying(36) NOT NULL,
    owner character varying(255) NOT NULL,
    requester character varying(255) NOT NULL,
    created_timestamp bigint NOT NULL,
    granted_timestamp bigint,
    resource_id character varying(36) NOT NULL,
    scope_id character varying(36),
    resource_server_id character varying(36) NOT NULL,
    policy_id character varying(36)
);


ALTER TABLE public.resource_server_perm_ticket OWNER TO keycloak_user;

--
-- TOC entry 265 (class 1259 OID 17436)
-- Name: resource_server_policy; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_server_policy (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    type character varying(255) NOT NULL,
    decision_strategy character varying(20),
    logic character varying(20),
    resource_server_id character varying(36) NOT NULL,
    owner character varying(255)
);


ALTER TABLE public.resource_server_policy OWNER TO keycloak_user;

--
-- TOC entry 263 (class 1259 OID 17406)
-- Name: resource_server_resource; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_server_resource (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    type character varying(255),
    icon_uri character varying(255),
    owner character varying(255) NOT NULL,
    resource_server_id character varying(36) NOT NULL,
    owner_managed_access boolean DEFAULT false NOT NULL,
    display_name character varying(255)
);


ALTER TABLE public.resource_server_resource OWNER TO keycloak_user;

--
-- TOC entry 264 (class 1259 OID 17421)
-- Name: resource_server_scope; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_server_scope (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    icon_uri character varying(255),
    resource_server_id character varying(36) NOT NULL,
    display_name character varying(255)
);


ALTER TABLE public.resource_server_scope OWNER TO keycloak_user;

--
-- TOC entry 289 (class 1259 OID 17925)
-- Name: resource_uris; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.resource_uris (
    resource_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.resource_uris OWNER TO keycloak_user;

--
-- TOC entry 290 (class 1259 OID 17935)
-- Name: role_attribute; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.role_attribute (
    id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255)
);


ALTER TABLE public.role_attribute OWNER TO keycloak_user;

--
-- TOC entry 215 (class 1259 OID 16500)
-- Name: scope_mapping; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.scope_mapping (
    client_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE public.scope_mapping OWNER TO keycloak_user;

--
-- TOC entry 269 (class 1259 OID 17494)
-- Name: scope_policy; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.scope_policy (
    scope_id character varying(36) NOT NULL,
    policy_id character varying(36) NOT NULL
);


ALTER TABLE public.scope_policy OWNER TO keycloak_user;

--
-- TOC entry 217 (class 1259 OID 16506)
-- Name: user_attribute; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_attribute (
    name character varying(255) NOT NULL,
    value character varying(255),
    user_id character varying(36) NOT NULL,
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL
);


ALTER TABLE public.user_attribute OWNER TO keycloak_user;

--
-- TOC entry 239 (class 1259 OID 16959)
-- Name: user_consent; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_consent (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    user_id character varying(36) NOT NULL,
    created_date bigint,
    last_updated_date bigint,
    client_storage_provider character varying(36),
    external_client_id character varying(255)
);


ALTER TABLE public.user_consent OWNER TO keycloak_user;

--
-- TOC entry 285 (class 1259 OID 17857)
-- Name: user_consent_client_scope; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_consent_client_scope (
    user_consent_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE public.user_consent_client_scope OWNER TO keycloak_user;

--
-- TOC entry 218 (class 1259 OID 16512)
-- Name: user_entity; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_entity (
    id character varying(36) NOT NULL,
    email character varying(255),
    email_constraint character varying(255),
    email_verified boolean DEFAULT false NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    federation_link character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    realm_id character varying(255),
    username character varying(255),
    created_timestamp bigint,
    service_account_client_link character varying(255),
    not_before integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.user_entity OWNER TO keycloak_user;

--
-- TOC entry 219 (class 1259 OID 16521)
-- Name: user_federation_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_federation_config (
    user_federation_provider_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.user_federation_config OWNER TO keycloak_user;

--
-- TOC entry 246 (class 1259 OID 17075)
-- Name: user_federation_mapper; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_federation_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    federation_provider_id character varying(36) NOT NULL,
    federation_mapper_type character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.user_federation_mapper OWNER TO keycloak_user;

--
-- TOC entry 247 (class 1259 OID 17081)
-- Name: user_federation_mapper_config; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_federation_mapper_config (
    user_federation_mapper_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.user_federation_mapper_config OWNER TO keycloak_user;

--
-- TOC entry 220 (class 1259 OID 16527)
-- Name: user_federation_provider; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_federation_provider (
    id character varying(36) NOT NULL,
    changed_sync_period integer,
    display_name character varying(255),
    full_sync_period integer,
    last_sync integer,
    priority integer,
    provider_name character varying(255),
    realm_id character varying(36)
);


ALTER TABLE public.user_federation_provider OWNER TO keycloak_user;

--
-- TOC entry 257 (class 1259 OID 17251)
-- Name: user_group_membership; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_group_membership (
    group_id character varying(36) NOT NULL,
    user_id character varying(36) NOT NULL
);


ALTER TABLE public.user_group_membership OWNER TO keycloak_user;

--
-- TOC entry 221 (class 1259 OID 16533)
-- Name: user_required_action; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_required_action (
    user_id character varying(36) NOT NULL,
    required_action character varying(255) DEFAULT ' '::character varying NOT NULL
);


ALTER TABLE public.user_required_action OWNER TO keycloak_user;

--
-- TOC entry 222 (class 1259 OID 16536)
-- Name: user_role_mapping; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_role_mapping (
    role_id character varying(255) NOT NULL,
    user_id character varying(36) NOT NULL
);


ALTER TABLE public.user_role_mapping OWNER TO keycloak_user;

--
-- TOC entry 223 (class 1259 OID 16539)
-- Name: user_session; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_session (
    id character varying(36) NOT NULL,
    auth_method character varying(255),
    ip_address character varying(255),
    last_session_refresh integer,
    login_username character varying(255),
    realm_id character varying(255),
    remember_me boolean DEFAULT false NOT NULL,
    started integer,
    user_id character varying(255),
    user_session_state integer,
    broker_session_id character varying(255),
    broker_user_id character varying(255)
);


ALTER TABLE public.user_session OWNER TO keycloak_user;

--
-- TOC entry 234 (class 1259 OID 16854)
-- Name: user_session_note; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.user_session_note (
    user_session character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(2048)
);


ALTER TABLE public.user_session_note OWNER TO keycloak_user;

--
-- TOC entry 216 (class 1259 OID 16503)
-- Name: username_login_failure; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.username_login_failure (
    realm_id character varying(36) NOT NULL,
    username character varying(255) NOT NULL,
    failed_login_not_before integer,
    last_failure bigint,
    last_ip_failure character varying(255),
    num_failures integer
);


ALTER TABLE public.username_login_failure OWNER TO keycloak_user;

--
-- TOC entry 224 (class 1259 OID 16552)
-- Name: web_origins; Type: TABLE; Schema: public; Owner: keycloak_user
--

CREATE TABLE public.web_origins (
    client_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.web_origins OWNER TO keycloak_user;

--
-- TOC entry 3882 (class 0 OID 17046)
-- Dependencies: 241
-- Data for Name: admin_event_entity; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3911 (class 0 OID 17509)
-- Dependencies: 270
-- Data for Name: associated_policy; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3885 (class 0 OID 17064)
-- Dependencies: 244
-- Data for Name: authentication_execution; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('44609c65-2a10-4a69-bbc7-2ca7cdeb5a31', NULL, 'auth-cookie', 'master', 'e6b99665-8fdd-485e-b907-0a560125c614', 2, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('ec3aaea2-41f4-42ca-b9e1-12bb262cdd82', NULL, 'auth-spnego', 'master', 'e6b99665-8fdd-485e-b907-0a560125c614', 3, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('b81e426e-5326-443c-b4a2-b036f77d1dfa', NULL, 'identity-provider-redirector', 'master', 'e6b99665-8fdd-485e-b907-0a560125c614', 2, 25, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('270ac08f-1682-403a-aca3-3b759f347295', NULL, NULL, 'master', 'e6b99665-8fdd-485e-b907-0a560125c614', 2, 30, true, '575ececc-820e-441d-b0c3-557c6375fdbe', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('431cb3c7-b85a-4af7-8062-ca119fdf3c52', NULL, 'auth-username-password-form', 'master', '575ececc-820e-441d-b0c3-557c6375fdbe', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('419e3cad-d6d6-4c32-bb2d-49b33eb931e3', NULL, NULL, 'master', '575ececc-820e-441d-b0c3-557c6375fdbe', 1, 20, true, 'aad62264-c3dd-4b2f-87d7-998d2a1666e9', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('74b80648-0d19-4d95-98d7-b4174579c8f7', NULL, 'conditional-user-configured', 'master', 'aad62264-c3dd-4b2f-87d7-998d2a1666e9', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('2af48c43-1cd2-4a7b-86f1-8da28631da11', NULL, 'auth-otp-form', 'master', 'aad62264-c3dd-4b2f-87d7-998d2a1666e9', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('5d073d68-3209-4c2e-aa44-549dcac8161a', NULL, 'direct-grant-validate-username', 'master', 'e8070c89-1270-4be4-9088-6806e74a62f2', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('987a4869-4986-4398-bf12-9fbc6c6a3f82', NULL, 'direct-grant-validate-password', 'master', 'e8070c89-1270-4be4-9088-6806e74a62f2', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('97d0481f-55f6-42fe-a274-b7271066a1cf', NULL, NULL, 'master', 'e8070c89-1270-4be4-9088-6806e74a62f2', 1, 30, true, 'c80c89a8-26dc-4886-a651-d07e241f5a06', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('3689f50f-44c5-4260-bd0c-9105e87153f9', NULL, 'conditional-user-configured', 'master', 'c80c89a8-26dc-4886-a651-d07e241f5a06', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('67bf3837-2198-404b-8ce8-2d77d67c42b8', NULL, 'direct-grant-validate-otp', 'master', 'c80c89a8-26dc-4886-a651-d07e241f5a06', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('b3b9b770-666f-43f3-b19a-bf639f830f14', NULL, 'registration-page-form', 'master', 'd28e8a0f-a23b-4d73-bc51-ba3943d430d6', 0, 10, true, 'b218ac39-05de-4768-97de-31c60ca7d340', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('327cf489-16b2-4ef8-9d87-3408b55886ad', NULL, 'registration-user-creation', 'master', 'b218ac39-05de-4768-97de-31c60ca7d340', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('b3eea9bb-e020-48e5-8cce-50c688a8f015', NULL, 'registration-profile-action', 'master', 'b218ac39-05de-4768-97de-31c60ca7d340', 0, 40, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('159c6517-726f-4002-9f4e-15ea656c2f32', NULL, 'registration-password-action', 'master', 'b218ac39-05de-4768-97de-31c60ca7d340', 0, 50, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('55c7b9a5-069f-4fbe-8c38-56f314f44e9c', NULL, 'registration-recaptcha-action', 'master', 'b218ac39-05de-4768-97de-31c60ca7d340', 3, 60, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('bbcfc23d-64f0-4b65-bfd4-44e551c707c2', NULL, 'reset-credentials-choose-user', 'master', '921fc4a2-d27b-4bde-99e3-4f27eb7d989a', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('3de41456-6c77-49d6-9ee9-4395564db15c', NULL, 'reset-credential-email', 'master', '921fc4a2-d27b-4bde-99e3-4f27eb7d989a', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('d8fcc6cd-65e4-442f-bc1e-804ecdb1b18e', NULL, 'reset-password', 'master', '921fc4a2-d27b-4bde-99e3-4f27eb7d989a', 0, 30, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('bfa64d63-17db-47f8-904a-61cf197e6d03', NULL, NULL, 'master', '921fc4a2-d27b-4bde-99e3-4f27eb7d989a', 1, 40, true, '2d7b2d5e-9bfb-4bb0-9590-a176946e7e52', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('287b7d35-a953-424e-937b-ad8568da6177', NULL, 'conditional-user-configured', 'master', '2d7b2d5e-9bfb-4bb0-9590-a176946e7e52', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e399787a-d04f-4f08-a118-b0b10bca88e9', NULL, 'reset-otp', 'master', '2d7b2d5e-9bfb-4bb0-9590-a176946e7e52', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('ef00beb9-670b-4879-98ce-b34f14cbb3b9', NULL, 'client-secret', 'master', '020d98e6-cd98-41bf-9bb0-d6059d0b61e9', 2, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('a3e04ddf-ed93-4ed6-b4bc-ea7f5016120f', NULL, 'client-jwt', 'master', '020d98e6-cd98-41bf-9bb0-d6059d0b61e9', 2, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e597a500-3c46-4207-a3d9-a9837b494c4a', NULL, 'client-secret-jwt', 'master', '020d98e6-cd98-41bf-9bb0-d6059d0b61e9', 2, 30, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('fc0fd4ed-9e9d-415d-b984-68754073e89a', NULL, 'client-x509', 'master', '020d98e6-cd98-41bf-9bb0-d6059d0b61e9', 2, 40, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('6c8d4829-549d-42f2-a071-1abd713f7c08', NULL, 'idp-review-profile', 'master', '9b5bd841-f0ad-473f-a607-ef5fb3ee413b', 0, 10, false, NULL, '932b316e-fda3-445c-8c75-514b83be2a72');
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('786abc63-0962-4d8c-ae1b-5003f0899032', NULL, NULL, 'master', '9b5bd841-f0ad-473f-a607-ef5fb3ee413b', 0, 20, true, '39236845-cdfe-4611-847d-f19398da294d', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('3e34079f-d870-40ed-85fa-c23499da70a3', NULL, 'idp-create-user-if-unique', 'master', '39236845-cdfe-4611-847d-f19398da294d', 2, 10, false, NULL, 'b5be3f29-6d8d-493a-bc09-291fc94080e2');
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('33650ed7-8405-4d6e-bd83-5de02c46e12b', NULL, NULL, 'master', '39236845-cdfe-4611-847d-f19398da294d', 2, 20, true, 'de50cc41-8d46-4635-8392-014392985d70', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('cd086c74-f4e0-466a-8585-a7763cc04486', NULL, 'idp-confirm-link', 'master', 'de50cc41-8d46-4635-8392-014392985d70', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('0e9edbfa-cffd-4414-b383-fccb211a2a19', NULL, NULL, 'master', 'de50cc41-8d46-4635-8392-014392985d70', 0, 20, true, '5c199b07-f79a-45e9-bdd8-66bf3219a66a', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('97aa08cb-a6ed-4f7e-a3ea-af2ec6b840a7', NULL, 'idp-email-verification', 'master', '5c199b07-f79a-45e9-bdd8-66bf3219a66a', 2, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('34f8b8c1-c86b-4aa5-9c6e-9536f1d2560c', NULL, NULL, 'master', '5c199b07-f79a-45e9-bdd8-66bf3219a66a', 2, 20, true, '8c50cf20-35bb-4789-b8e0-79c88731d0cf', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('378f2b08-1de8-4ce0-920a-40d92d9640ad', NULL, 'idp-username-password-form', 'master', '8c50cf20-35bb-4789-b8e0-79c88731d0cf', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('f899d722-5dc7-4e96-a63f-31f135a0fb3e', NULL, NULL, 'master', '8c50cf20-35bb-4789-b8e0-79c88731d0cf', 1, 20, true, '9e93241f-faed-4930-ad5d-ceb51afe1d88', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('bd69f1e1-2a67-4f52-b768-ca5d048c7c9c', NULL, 'conditional-user-configured', 'master', '9e93241f-faed-4930-ad5d-ceb51afe1d88', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('1db7c640-8195-4e43-b11f-14bdf8a1a9f3', NULL, 'auth-otp-form', 'master', '9e93241f-faed-4930-ad5d-ceb51afe1d88', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('4bb29274-28a1-4270-af44-cb957a3d0935', NULL, 'http-basic-authenticator', 'master', 'c07714f8-0bb9-47c6-aced-357bd2ca9932', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('36215f84-ad90-4ea0-b8cd-1bebafbdca7a', NULL, 'docker-http-basic-authenticator', 'master', '3a60391a-815b-4f4e-86a8-6e7159c76e9a', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('cb2a604d-0965-4f5a-8244-7223099d4e69', NULL, 'no-cookie-redirect', 'master', '5300d754-438a-4100-ab96-572d9e28f95e', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e586a1b0-8500-4a06-998b-35850850c971', NULL, NULL, 'master', '5300d754-438a-4100-ab96-572d9e28f95e', 0, 20, true, 'e6f40cc6-ff13-42e0-86db-1ebe8e3b5f3e', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('406c1f03-bf54-4976-b1c6-c6fcaf11cea0', NULL, 'basic-auth', 'master', 'e6f40cc6-ff13-42e0-86db-1ebe8e3b5f3e', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('48e302a9-a7ce-4745-b872-271ac2fb49f4', NULL, 'basic-auth-otp', 'master', 'e6f40cc6-ff13-42e0-86db-1ebe8e3b5f3e', 3, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('5a659807-f20f-4cf0-ba62-8909ba97cbc7', NULL, 'auth-spnego', 'master', 'e6f40cc6-ff13-42e0-86db-1ebe8e3b5f3e', 3, 30, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('0e6befce-7b11-473a-81c3-d7ccda47d978', NULL, 'auth-cookie', 'university', '52558793-ede5-4c63-bb75-e315dc679d5b', 2, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('7624d4ce-08ba-486a-879f-ac09f5f55b2e', NULL, 'auth-spnego', 'university', '52558793-ede5-4c63-bb75-e315dc679d5b', 3, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e7b95777-4ff8-4483-aeb4-1ea8f846e04a', NULL, 'identity-provider-redirector', 'university', '52558793-ede5-4c63-bb75-e315dc679d5b', 2, 25, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('d076a148-bc9a-47b9-815c-f7c58d2b1bff', NULL, NULL, 'university', '52558793-ede5-4c63-bb75-e315dc679d5b', 2, 30, true, '9cf0ac03-7dd0-4359-b7a1-5dc8db206cbd', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('1de85d85-fa48-4149-ba9a-db52e3fd1d58', NULL, 'auth-username-password-form', 'university', '9cf0ac03-7dd0-4359-b7a1-5dc8db206cbd', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('9a7259a2-812b-4594-ba3c-8eb65a5daf02', NULL, NULL, 'university', '9cf0ac03-7dd0-4359-b7a1-5dc8db206cbd', 1, 20, true, '10fa7797-ca8c-48f9-8843-5b82b0262979', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e3cdf2f2-7010-47ae-8128-d4075e27d5b8', NULL, 'conditional-user-configured', 'university', '10fa7797-ca8c-48f9-8843-5b82b0262979', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('2b486a8a-d540-463d-8cb9-bf856222832d', NULL, 'auth-otp-form', 'university', '10fa7797-ca8c-48f9-8843-5b82b0262979', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('68527487-bc17-49a1-b17e-f045a70072e3', NULL, 'direct-grant-validate-username', 'university', '68821da6-a750-490d-b70a-a7db66e8cf7f', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('a861f451-76bb-45b2-ae2e-a97a1e9ee858', NULL, 'direct-grant-validate-password', 'university', '68821da6-a750-490d-b70a-a7db66e8cf7f', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e94c21ea-c17f-45d5-8e50-64509b14d29a', NULL, NULL, 'university', '68821da6-a750-490d-b70a-a7db66e8cf7f', 1, 30, true, 'e90bd468-9376-45e3-b1ed-b37e555a40d0', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('b89f201c-7841-49bb-8316-36c5ab170edd', NULL, 'conditional-user-configured', 'university', 'e90bd468-9376-45e3-b1ed-b37e555a40d0', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('23e689b4-bd11-47b5-8da3-be2c043e157c', NULL, 'direct-grant-validate-otp', 'university', 'e90bd468-9376-45e3-b1ed-b37e555a40d0', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('26bd1fc6-f442-4c3c-be5e-a10ccbcabd48', NULL, 'registration-page-form', 'university', '489811e4-a562-4d76-b0ae-5426ec57001b', 0, 10, true, 'b39a5896-e722-4d52-aa0c-a2bb74839315', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('977eb6c8-aa6e-45ff-b66b-36eb7dac8f23', NULL, 'registration-user-creation', 'university', 'b39a5896-e722-4d52-aa0c-a2bb74839315', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('7de801f5-13b2-40b5-8816-f098fadd657c', NULL, 'registration-profile-action', 'university', 'b39a5896-e722-4d52-aa0c-a2bb74839315', 0, 40, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('a839d72c-d8d4-4649-a9e6-ccfc83760621', NULL, 'registration-password-action', 'university', 'b39a5896-e722-4d52-aa0c-a2bb74839315', 0, 50, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('4d615b49-d836-4489-9a08-08ac315aeea5', NULL, 'registration-recaptcha-action', 'university', 'b39a5896-e722-4d52-aa0c-a2bb74839315', 3, 60, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('51b8080c-8e5f-4c23-bb52-ca4d60a7a36b', NULL, 'reset-credentials-choose-user', 'university', 'bba4b5f3-2103-44a4-84ef-a7d9da97cc3a', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('8e5681ed-ff22-438e-bc47-81cffd7207ae', NULL, 'reset-credential-email', 'university', 'bba4b5f3-2103-44a4-84ef-a7d9da97cc3a', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('a3055e5d-0743-40f1-bbb6-8f39a533ba3a', NULL, 'reset-password', 'university', 'bba4b5f3-2103-44a4-84ef-a7d9da97cc3a', 0, 30, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('b8547e08-e32e-463c-bcb6-ac1c9ba36579', NULL, NULL, 'university', 'bba4b5f3-2103-44a4-84ef-a7d9da97cc3a', 1, 40, true, '3699de13-06b1-4a54-bcee-eb844d24e6f4', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('a2ed76e7-c1e5-479b-812a-c97782a83710', NULL, 'conditional-user-configured', 'university', '3699de13-06b1-4a54-bcee-eb844d24e6f4', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('68d0f96c-c581-4a27-9361-15f6dfd9243b', NULL, 'reset-otp', 'university', '3699de13-06b1-4a54-bcee-eb844d24e6f4', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('1279702c-55cd-43f7-a939-faf87a9d3da2', NULL, 'client-secret', 'university', '6f779007-976d-4020-8b43-ded2b8b65a72', 2, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('18552af7-da8a-4c6f-98f6-69bc2e6b448a', NULL, 'client-jwt', 'university', '6f779007-976d-4020-8b43-ded2b8b65a72', 2, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('b9335a73-5ec4-4941-9078-18f094896b7f', NULL, 'client-secret-jwt', 'university', '6f779007-976d-4020-8b43-ded2b8b65a72', 2, 30, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('f0885549-2aa8-49e5-89aa-83d87586559e', NULL, 'client-x509', 'university', '6f779007-976d-4020-8b43-ded2b8b65a72', 2, 40, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('85992e3c-0a20-4794-af3b-f5326dbd4d9b', NULL, 'idp-review-profile', 'university', '067a47d8-bc51-47e7-89a4-a4a8bca138d8', 0, 10, false, NULL, '4a09adf6-c5e7-4d94-81cf-24af2f38e644');
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('b69d5ec4-e153-4fe9-b8bc-02bab8afcc91', NULL, NULL, 'university', '067a47d8-bc51-47e7-89a4-a4a8bca138d8', 0, 20, true, 'cff6afb2-41f6-4a11-b6ba-f0119cd4f6a7', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('ce881a09-3260-4e16-8a0c-7b73995592ae', NULL, 'idp-create-user-if-unique', 'university', 'cff6afb2-41f6-4a11-b6ba-f0119cd4f6a7', 2, 10, false, NULL, '59c82747-840b-4f0c-a9e5-4d96f5b292ea');
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('0f59c68a-fbfe-4f1f-825a-a8c7e2af17b1', NULL, NULL, 'university', 'cff6afb2-41f6-4a11-b6ba-f0119cd4f6a7', 2, 20, true, '530017f2-b75a-4b49-aab7-5769e3e0c4d0', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('f3aa8042-9bf3-482f-bab7-50d1bda86851', NULL, 'idp-confirm-link', 'university', '530017f2-b75a-4b49-aab7-5769e3e0c4d0', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('cf2bcc3e-3b37-4205-a75e-ddd8d854da87', NULL, NULL, 'university', '530017f2-b75a-4b49-aab7-5769e3e0c4d0', 0, 20, true, 'd79e0da7-2645-40a3-9d75-13dc0a0b0a61', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e65e7b71-53dc-4072-8810-172b3c9b6c30', NULL, 'idp-email-verification', 'university', 'd79e0da7-2645-40a3-9d75-13dc0a0b0a61', 2, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('0e083000-eb17-4ece-9ea7-42d95db27003', NULL, NULL, 'university', 'd79e0da7-2645-40a3-9d75-13dc0a0b0a61', 2, 20, true, '651cffdd-c682-4df6-abf7-ddc50be6767e', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('66e078a5-690c-4d91-a09d-bf400132991c', NULL, 'idp-username-password-form', 'university', '651cffdd-c682-4df6-abf7-ddc50be6767e', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e6ffa79a-2aaf-42d8-a5e7-342609c5892c', NULL, NULL, 'university', '651cffdd-c682-4df6-abf7-ddc50be6767e', 1, 20, true, 'eda67e40-1654-4584-8ba8-eaaff06647c5', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('1e911820-3540-4fd5-ab3f-64e84ef9759f', NULL, 'conditional-user-configured', 'university', 'eda67e40-1654-4584-8ba8-eaaff06647c5', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('dffecbb3-9586-4647-a8cc-9ff9bb7f65ab', NULL, 'auth-otp-form', 'university', 'eda67e40-1654-4584-8ba8-eaaff06647c5', 0, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('06a51168-b770-461b-a922-d519716dab33', NULL, 'http-basic-authenticator', 'university', 'eabfd236-2bd5-4d3f-9ab3-c3784c09a92d', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('81559bdc-bc27-4eda-bfae-26db7816acca', NULL, 'docker-http-basic-authenticator', 'university', 'bf3b9371-4ea3-4134-9462-99c4080c486c', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('1b5a10f5-4b5b-4b88-8fff-633c0c614dfd', NULL, 'no-cookie-redirect', 'university', '15735a6b-b30e-46b3-8d86-a67bd59af874', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('01eef774-af94-4170-89b8-1d275a436429', NULL, NULL, 'university', '15735a6b-b30e-46b3-8d86-a67bd59af874', 0, 20, true, 'be1b21a6-f9bc-4ab8-9172-26405ef6b2a9', NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('f9377184-bf81-489c-b03a-cfe419cd1a5d', NULL, 'basic-auth', 'university', 'be1b21a6-f9bc-4ab8-9172-26405ef6b2a9', 0, 10, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('105b2d00-ac4c-4159-8086-d9249c3058ed', NULL, 'basic-auth-otp', 'university', 'be1b21a6-f9bc-4ab8-9172-26405ef6b2a9', 3, 20, false, NULL, NULL);
INSERT INTO public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) VALUES ('e0c5b0b4-a3a7-49fe-a811-9056a4894114', NULL, 'auth-spnego', 'university', 'be1b21a6-f9bc-4ab8-9172-26405ef6b2a9', 3, 30, false, NULL, NULL);


--
-- TOC entry 3884 (class 0 OID 17058)
-- Dependencies: 243
-- Data for Name: authentication_flow; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('e6b99665-8fdd-485e-b907-0a560125c614', 'browser', 'browser based authentication', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('575ececc-820e-441d-b0c3-557c6375fdbe', 'forms', 'Username, password, otp and other auth forms.', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('aad62264-c3dd-4b2f-87d7-998d2a1666e9', 'Browser - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('e8070c89-1270-4be4-9088-6806e74a62f2', 'direct grant', 'OpenID Connect Resource Owner Grant', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('c80c89a8-26dc-4886-a651-d07e241f5a06', 'Direct Grant - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('d28e8a0f-a23b-4d73-bc51-ba3943d430d6', 'registration', 'registration flow', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('b218ac39-05de-4768-97de-31c60ca7d340', 'registration form', 'registration form', 'master', 'form-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('921fc4a2-d27b-4bde-99e3-4f27eb7d989a', 'reset credentials', 'Reset credentials for a user if they forgot their password or something', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('2d7b2d5e-9bfb-4bb0-9590-a176946e7e52', 'Reset - Conditional OTP', 'Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('020d98e6-cd98-41bf-9bb0-d6059d0b61e9', 'clients', 'Base authentication for clients', 'master', 'client-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('9b5bd841-f0ad-473f-a607-ef5fb3ee413b', 'first broker login', 'Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('39236845-cdfe-4611-847d-f19398da294d', 'User creation or linking', 'Flow for the existing/non-existing user alternatives', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('de50cc41-8d46-4635-8392-014392985d70', 'Handle Existing Account', 'Handle what to do if there is existing account with same email/username like authenticated identity provider', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('5c199b07-f79a-45e9-bdd8-66bf3219a66a', 'Account verification options', 'Method with which to verity the existing account', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('8c50cf20-35bb-4789-b8e0-79c88731d0cf', 'Verify Existing Account by Re-authentication', 'Reauthentication of existing account', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('9e93241f-faed-4930-ad5d-ceb51afe1d88', 'First broker login - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('c07714f8-0bb9-47c6-aced-357bd2ca9932', 'saml ecp', 'SAML ECP Profile Authentication Flow', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('3a60391a-815b-4f4e-86a8-6e7159c76e9a', 'docker auth', 'Used by Docker clients to authenticate against the IDP', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('5300d754-438a-4100-ab96-572d9e28f95e', 'http challenge', 'An authentication flow based on challenge-response HTTP Authentication Schemes', 'master', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('e6f40cc6-ff13-42e0-86db-1ebe8e3b5f3e', 'Authentication Options', 'Authentication options.', 'master', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('52558793-ede5-4c63-bb75-e315dc679d5b', 'browser', 'browser based authentication', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('9cf0ac03-7dd0-4359-b7a1-5dc8db206cbd', 'forms', 'Username, password, otp and other auth forms.', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('10fa7797-ca8c-48f9-8843-5b82b0262979', 'Browser - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('68821da6-a750-490d-b70a-a7db66e8cf7f', 'direct grant', 'OpenID Connect Resource Owner Grant', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('e90bd468-9376-45e3-b1ed-b37e555a40d0', 'Direct Grant - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('489811e4-a562-4d76-b0ae-5426ec57001b', 'registration', 'registration flow', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('b39a5896-e722-4d52-aa0c-a2bb74839315', 'registration form', 'registration form', 'university', 'form-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('bba4b5f3-2103-44a4-84ef-a7d9da97cc3a', 'reset credentials', 'Reset credentials for a user if they forgot their password or something', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('3699de13-06b1-4a54-bcee-eb844d24e6f4', 'Reset - Conditional OTP', 'Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('6f779007-976d-4020-8b43-ded2b8b65a72', 'clients', 'Base authentication for clients', 'university', 'client-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('067a47d8-bc51-47e7-89a4-a4a8bca138d8', 'first broker login', 'Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('cff6afb2-41f6-4a11-b6ba-f0119cd4f6a7', 'User creation or linking', 'Flow for the existing/non-existing user alternatives', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('530017f2-b75a-4b49-aab7-5769e3e0c4d0', 'Handle Existing Account', 'Handle what to do if there is existing account with same email/username like authenticated identity provider', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('d79e0da7-2645-40a3-9d75-13dc0a0b0a61', 'Account verification options', 'Method with which to verity the existing account', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('651cffdd-c682-4df6-abf7-ddc50be6767e', 'Verify Existing Account by Re-authentication', 'Reauthentication of existing account', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('eda67e40-1654-4584-8ba8-eaaff06647c5', 'First broker login - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'university', 'basic-flow', false, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('eabfd236-2bd5-4d3f-9ab3-c3784c09a92d', 'saml ecp', 'SAML ECP Profile Authentication Flow', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('bf3b9371-4ea3-4134-9462-99c4080c486c', 'docker auth', 'Used by Docker clients to authenticate against the IDP', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('15735a6b-b30e-46b3-8d86-a67bd59af874', 'http challenge', 'An authentication flow based on challenge-response HTTP Authentication Schemes', 'university', 'basic-flow', true, true);
INSERT INTO public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) VALUES ('be1b21a6-f9bc-4ab8-9172-26405ef6b2a9', 'Authentication Options', 'Authentication options.', 'university', 'basic-flow', false, true);


--
-- TOC entry 3883 (class 0 OID 17052)
-- Dependencies: 242
-- Data for Name: authenticator_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.authenticator_config (id, alias, realm_id) VALUES ('932b316e-fda3-445c-8c75-514b83be2a72', 'review profile config', 'master');
INSERT INTO public.authenticator_config (id, alias, realm_id) VALUES ('b5be3f29-6d8d-493a-bc09-291fc94080e2', 'create unique user config', 'master');
INSERT INTO public.authenticator_config (id, alias, realm_id) VALUES ('4a09adf6-c5e7-4d94-81cf-24af2f38e644', 'review profile config', 'university');
INSERT INTO public.authenticator_config (id, alias, realm_id) VALUES ('59c82747-840b-4f0c-a9e5-4d96f5b292ea', 'create unique user config', 'university');


--
-- TOC entry 3886 (class 0 OID 17069)
-- Dependencies: 245
-- Data for Name: authenticator_config_entry; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.authenticator_config_entry (authenticator_id, value, name) VALUES ('932b316e-fda3-445c-8c75-514b83be2a72', 'missing', 'update.profile.on.first.login');
INSERT INTO public.authenticator_config_entry (authenticator_id, value, name) VALUES ('b5be3f29-6d8d-493a-bc09-291fc94080e2', 'false', 'require.password.update.after.registration');
INSERT INTO public.authenticator_config_entry (authenticator_id, value, name) VALUES ('4a09adf6-c5e7-4d94-81cf-24af2f38e644', 'missing', 'update.profile.on.first.login');
INSERT INTO public.authenticator_config_entry (authenticator_id, value, name) VALUES ('59c82747-840b-4f0c-a9e5-4d96f5b292ea', 'false', 'require.password.update.after.registration');


--
-- TOC entry 3912 (class 0 OID 17524)
-- Dependencies: 271
-- Data for Name: broker_link; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3843 (class 0 OID 16401)
-- Dependencies: 202
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, false, 'master-realm', 0, false, NULL, NULL, true, NULL, false, 'master', NULL, 0, false, false, 'master Realm', false, 'client-secret', NULL, NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', true, false, 'account', 0, true, NULL, '/realms/master/account/', false, NULL, false, 'master', 'openid-connect', 0, false, false, '${client_account}', false, 'client-secret', '${authBaseUrl}', NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', true, false, 'account-console', 0, true, NULL, '/realms/master/account/', false, NULL, false, 'master', 'openid-connect', 0, false, false, '${client_account-console}', false, 'client-secret', '${authBaseUrl}', NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', true, false, 'broker', 0, false, NULL, NULL, true, NULL, false, 'master', 'openid-connect', 0, false, false, '${client_broker}', false, 'client-secret', NULL, NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', true, false, 'security-admin-console', 0, true, NULL, '/admin/master/console/', false, NULL, false, 'master', 'openid-connect', 0, false, false, '${client_security-admin-console}', false, 'client-secret', '${authAdminUrl}', NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', true, false, 'admin-cli', 0, true, NULL, NULL, false, NULL, false, 'master', 'openid-connect', 0, false, false, '${client_admin-cli}', false, 'client-secret', NULL, NULL, NULL, false, false, true, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('92c10070-548c-4879-be84-b271f89f3223', true, false, 'university-realm', 0, false, NULL, NULL, true, NULL, false, 'master', NULL, 0, false, false, 'university Realm', false, 'client-secret', NULL, NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', true, false, 'realm-management', 0, false, NULL, NULL, true, NULL, false, 'university', 'openid-connect', 0, false, false, '${client_realm-management}', false, 'client-secret', NULL, NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, false, 'account', 0, true, NULL, '/realms/university/account/', false, NULL, false, 'university', 'openid-connect', 0, false, false, '${client_account}', false, 'client-secret', '${authBaseUrl}', NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', true, false, 'account-console', 0, true, NULL, '/realms/university/account/', false, NULL, false, 'university', 'openid-connect', 0, false, false, '${client_account-console}', false, 'client-secret', '${authBaseUrl}', NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', true, false, 'broker', 0, false, NULL, NULL, true, NULL, false, 'university', 'openid-connect', 0, false, false, '${client_broker}', false, 'client-secret', NULL, NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', true, false, 'security-admin-console', 0, true, NULL, '/admin/university/console/', false, NULL, false, 'university', 'openid-connect', 0, false, false, '${client_security-admin-console}', false, 'client-secret', '${authAdminUrl}', NULL, NULL, true, false, false, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', true, false, 'admin-cli', 0, true, NULL, NULL, false, NULL, false, 'university', 'openid-connect', 0, false, false, '${client_admin-cli}', false, 'client-secret', NULL, NULL, NULL, false, false, true, false);
INSERT INTO public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', true, false, 'university_client', 0, false, 'iyDwn78IzYn45566AilXJ8IUKyhdPyJL', '/', false, NULL, false, 'university', 'openid-connect', -1, false, false, 'University', false, 'client-secret', 'http://localhost:8888/api', 'Management System', 'd5d49e92-6437-4b69-b637-068b1235227b', true, false, true, false);


--
-- TOC entry 3866 (class 0 OID 16775)
-- Dependencies: 225
-- Data for Name: client_attributes; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.client_attributes (client_id, value, name) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', 'S256', 'pkce.code.challenge.method');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', 'S256', 'pkce.code.challenge.method');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', 'S256', 'pkce.code.challenge.method');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', 'S256', 'pkce.code.challenge.method');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'true', 'backchannel.logout.session.required');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'backchannel.logout.revoke.offline.tokens');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.artifact.binding');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.server.signature');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.server.signature.keyinfo.ext');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.assertion.signature');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.client.signature');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.encrypt');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.authnstatement');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.onetimeuse.condition');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml_force_name_id_format');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.multivalued.roles');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'saml.force.post.binding');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'exclude.session.state.from.auth.response');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'oauth2.device.authorization.grant.enabled');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'oidc.ciba.grant.enabled');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'true', 'use.refresh.tokens');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'id.token.as.detached.signature');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'tls.client.certificate.bound.access.tokens');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'require.pushed.authorization.requests');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'client_credentials.use_refresh_token');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'false', 'display.on.consent.screen');
INSERT INTO public.client_attributes (client_id, value, name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'RS256', 'token.endpoint.auth.signing.alg');


--
-- TOC entry 3923 (class 0 OID 17783)
-- Dependencies: 282
-- Data for Name: client_auth_flow_bindings; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.client_auth_flow_bindings (client_id, flow_id, binding_name) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', '68821da6-a750-490d-b70a-a7db66e8cf7f', 'direct_grant');


--
-- TOC entry 3922 (class 0 OID 17658)
-- Dependencies: 281
-- Data for Name: client_initial_access; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3868 (class 0 OID 16787)
-- Dependencies: 227
-- Data for Name: client_node_registrations; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3900 (class 0 OID 17307)
-- Dependencies: 259
-- Data for Name: client_scope; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', 'offline_access', 'master', 'OpenID Connect built-in scope: offline_access', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('c18c5ada-6579-4a8c-ab75-0f23f563a2b6', 'role_list', 'master', 'SAML role list', 'saml');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', 'profile', 'master', 'OpenID Connect built-in scope: profile', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('0cef8854-46a9-4dc4-98c1-35b9d10d5a59', 'email', 'master', 'OpenID Connect built-in scope: email', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('d3905410-7155-4fad-a2a5-5155f64dee8a', 'address', 'master', 'OpenID Connect built-in scope: address', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('45b07521-a8a3-466c-a36d-ada9717ed9b4', 'phone', 'master', 'OpenID Connect built-in scope: phone', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('38de0fa7-5b2b-4cb9-921c-0f16a124eed1', 'roles', 'master', 'OpenID Connect scope for add user roles to the access token', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('d2708b0a-4401-4da9-abea-9ca33e2cd8db', 'web-origins', 'master', 'OpenID Connect scope for add allowed web origins to the access token', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('f3ed45d6-a944-4af5-aeb4-79291fabc9be', 'microprofile-jwt', 'master', 'Microprofile - JWT built-in scope', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('8ba7e175-1d33-4501-b2f5-9f924b5487cd', 'offline_access', 'university', 'OpenID Connect built-in scope: offline_access', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('057389b7-6253-43d7-ac0a-a57105bbf4e5', 'role_list', 'university', 'SAML role list', 'saml');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('18a5ab32-7d92-4063-8a40-e4859ef31db7', 'profile', 'university', 'OpenID Connect built-in scope: profile', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('a3337170-3392-413a-8d20-a441e0f70c2d', 'email', 'university', 'OpenID Connect built-in scope: email', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('641d3330-8b5e-4d73-963d-99cbe7c60ce4', 'address', 'university', 'OpenID Connect built-in scope: address', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('c23e5d45-4fe9-467c-b1e3-dad6763cb119', 'phone', 'university', 'OpenID Connect built-in scope: phone', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', 'roles', 'university', 'OpenID Connect scope for add user roles to the access token', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', 'web-origins', 'university', 'OpenID Connect scope for add allowed web origins to the access token', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('ea3fea9a-40e1-40fb-ba9c-da535647e706', 'microprofile-jwt', 'university', 'Microprofile - JWT built-in scope', 'openid-connect');
INSERT INTO public.client_scope (id, name, realm_id, description, protocol) VALUES ('418e9be3-8b8a-4542-8b9f-f965312bb7cc', 'university_scope', 'university', NULL, 'openid-connect');


--
-- TOC entry 3901 (class 0 OID 17322)
-- Dependencies: 260
-- Data for Name: client_scope_attributes; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', '${offlineAccessScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('c18c5ada-6579-4a8c-ab75-0f23f563a2b6', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('c18c5ada-6579-4a8c-ab75-0f23f563a2b6', '${samlRoleListScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', '${profileScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('0cef8854-46a9-4dc4-98c1-35b9d10d5a59', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('0cef8854-46a9-4dc4-98c1-35b9d10d5a59', '${emailScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('0cef8854-46a9-4dc4-98c1-35b9d10d5a59', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('d3905410-7155-4fad-a2a5-5155f64dee8a', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('d3905410-7155-4fad-a2a5-5155f64dee8a', '${addressScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('d3905410-7155-4fad-a2a5-5155f64dee8a', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('45b07521-a8a3-466c-a36d-ada9717ed9b4', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('45b07521-a8a3-466c-a36d-ada9717ed9b4', '${phoneScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('45b07521-a8a3-466c-a36d-ada9717ed9b4', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('38de0fa7-5b2b-4cb9-921c-0f16a124eed1', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('38de0fa7-5b2b-4cb9-921c-0f16a124eed1', '${rolesScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('38de0fa7-5b2b-4cb9-921c-0f16a124eed1', 'false', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('d2708b0a-4401-4da9-abea-9ca33e2cd8db', 'false', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('d2708b0a-4401-4da9-abea-9ca33e2cd8db', '', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('d2708b0a-4401-4da9-abea-9ca33e2cd8db', 'false', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('f3ed45d6-a944-4af5-aeb4-79291fabc9be', 'false', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('f3ed45d6-a944-4af5-aeb4-79291fabc9be', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('8ba7e175-1d33-4501-b2f5-9f924b5487cd', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('8ba7e175-1d33-4501-b2f5-9f924b5487cd', '${offlineAccessScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('057389b7-6253-43d7-ac0a-a57105bbf4e5', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('057389b7-6253-43d7-ac0a-a57105bbf4e5', '${samlRoleListScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('18a5ab32-7d92-4063-8a40-e4859ef31db7', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('18a5ab32-7d92-4063-8a40-e4859ef31db7', '${profileScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('18a5ab32-7d92-4063-8a40-e4859ef31db7', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('a3337170-3392-413a-8d20-a441e0f70c2d', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('a3337170-3392-413a-8d20-a441e0f70c2d', '${emailScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('a3337170-3392-413a-8d20-a441e0f70c2d', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('641d3330-8b5e-4d73-963d-99cbe7c60ce4', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('641d3330-8b5e-4d73-963d-99cbe7c60ce4', '${addressScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('641d3330-8b5e-4d73-963d-99cbe7c60ce4', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('c23e5d45-4fe9-467c-b1e3-dad6763cb119', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('c23e5d45-4fe9-467c-b1e3-dad6763cb119', '${phoneScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('c23e5d45-4fe9-467c-b1e3-dad6763cb119', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', '${rolesScopeConsentText}', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', 'false', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', 'false', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', '', 'consent.screen.text');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', 'false', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('ea3fea9a-40e1-40fb-ba9c-da535647e706', 'false', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('ea3fea9a-40e1-40fb-ba9c-da535647e706', 'true', 'include.in.token.scope');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('418e9be3-8b8a-4542-8b9f-f965312bb7cc', 'true', 'display.on.consent.screen');
INSERT INTO public.client_scope_attributes (scope_id, value, name) VALUES ('418e9be3-8b8a-4542-8b9f-f965312bb7cc', 'true', 'include.in.token.scope');


--
-- TOC entry 3924 (class 0 OID 17825)
-- Dependencies: 283
-- Data for Name: client_scope_client; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', 'd2708b0a-4401-4da9-abea-9ca33e2cd8db', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', '0cef8854-46a9-4dc4-98c1-35b9d10d5a59', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', '38de0fa7-5b2b-4cb9-921c-0f16a124eed1', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', 'e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', 'f3ed45d6-a944-4af5-aeb4-79291fabc9be', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', '45b07521-a8a3-466c-a36d-ada9717ed9b4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', 'd3905410-7155-4fad-a2a5-5155f64dee8a', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', 'd2708b0a-4401-4da9-abea-9ca33e2cd8db', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', '0cef8854-46a9-4dc4-98c1-35b9d10d5a59', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', '38de0fa7-5b2b-4cb9-921c-0f16a124eed1', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', 'e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', 'f3ed45d6-a944-4af5-aeb4-79291fabc9be', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', '45b07521-a8a3-466c-a36d-ada9717ed9b4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', 'd3905410-7155-4fad-a2a5-5155f64dee8a', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', 'd2708b0a-4401-4da9-abea-9ca33e2cd8db', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', '0cef8854-46a9-4dc4-98c1-35b9d10d5a59', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', '38de0fa7-5b2b-4cb9-921c-0f16a124eed1', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', 'e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', 'f3ed45d6-a944-4af5-aeb4-79291fabc9be', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', '45b07521-a8a3-466c-a36d-ada9717ed9b4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('831465e6-df60-4278-8b4b-76271f0e7fb1', 'd3905410-7155-4fad-a2a5-5155f64dee8a', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', 'd2708b0a-4401-4da9-abea-9ca33e2cd8db', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', '0cef8854-46a9-4dc4-98c1-35b9d10d5a59', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', '38de0fa7-5b2b-4cb9-921c-0f16a124eed1', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', 'e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', 'f3ed45d6-a944-4af5-aeb4-79291fabc9be', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', '45b07521-a8a3-466c-a36d-ada9717ed9b4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('06c77aa1-f92e-4376-82ba-0f73269e3f95', 'd3905410-7155-4fad-a2a5-5155f64dee8a', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', 'd2708b0a-4401-4da9-abea-9ca33e2cd8db', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', '0cef8854-46a9-4dc4-98c1-35b9d10d5a59', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', '38de0fa7-5b2b-4cb9-921c-0f16a124eed1', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', 'e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', 'f3ed45d6-a944-4af5-aeb4-79291fabc9be', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', '45b07521-a8a3-466c-a36d-ada9717ed9b4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5a51f87d-6c9d-400c-bb6a-7d028ec2be89', 'd3905410-7155-4fad-a2a5-5155f64dee8a', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', 'd2708b0a-4401-4da9-abea-9ca33e2cd8db', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', '0cef8854-46a9-4dc4-98c1-35b9d10d5a59', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', '38de0fa7-5b2b-4cb9-921c-0f16a124eed1', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', 'e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', 'f3ed45d6-a944-4af5-aeb4-79291fabc9be', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', '45b07521-a8a3-466c-a36d-ada9717ed9b4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', 'd3905410-7155-4fad-a2a5-5155f64dee8a', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', '6a47123f-6028-438d-9a9e-cb9ad77f4104', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', 'a3337170-3392-413a-8d20-a441e0f70c2d', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', '18a5ab32-7d92-4063-8a40-e4859ef31db7', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', '32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', 'ea3fea9a-40e1-40fb-ba9c-da535647e706', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', 'c23e5d45-4fe9-467c-b1e3-dad6763cb119', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', '8ba7e175-1d33-4501-b2f5-9f924b5487cd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', '641d3330-8b5e-4d73-963d-99cbe7c60ce4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', '6a47123f-6028-438d-9a9e-cb9ad77f4104', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', 'a3337170-3392-413a-8d20-a441e0f70c2d', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', '18a5ab32-7d92-4063-8a40-e4859ef31db7', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', '32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', 'ea3fea9a-40e1-40fb-ba9c-da535647e706', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', 'c23e5d45-4fe9-467c-b1e3-dad6763cb119', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', '8ba7e175-1d33-4501-b2f5-9f924b5487cd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', '641d3330-8b5e-4d73-963d-99cbe7c60ce4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', '6a47123f-6028-438d-9a9e-cb9ad77f4104', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', 'a3337170-3392-413a-8d20-a441e0f70c2d', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', '18a5ab32-7d92-4063-8a40-e4859ef31db7', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', '32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', 'ea3fea9a-40e1-40fb-ba9c-da535647e706', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', 'c23e5d45-4fe9-467c-b1e3-dad6763cb119', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', '8ba7e175-1d33-4501-b2f5-9f924b5487cd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('45af443a-03a7-4017-8e1e-2c0aed9e5c0f', '641d3330-8b5e-4d73-963d-99cbe7c60ce4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', '6a47123f-6028-438d-9a9e-cb9ad77f4104', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', 'a3337170-3392-413a-8d20-a441e0f70c2d', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', '18a5ab32-7d92-4063-8a40-e4859ef31db7', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', '32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', 'ea3fea9a-40e1-40fb-ba9c-da535647e706', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', 'c23e5d45-4fe9-467c-b1e3-dad6763cb119', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', '8ba7e175-1d33-4501-b2f5-9f924b5487cd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('2563eadc-22da-4cf0-b50b-cbae77e3e68e', '641d3330-8b5e-4d73-963d-99cbe7c60ce4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', '6a47123f-6028-438d-9a9e-cb9ad77f4104', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', 'a3337170-3392-413a-8d20-a441e0f70c2d', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', '18a5ab32-7d92-4063-8a40-e4859ef31db7', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', '32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', 'ea3fea9a-40e1-40fb-ba9c-da535647e706', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', 'c23e5d45-4fe9-467c-b1e3-dad6763cb119', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', '8ba7e175-1d33-4501-b2f5-9f924b5487cd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('35a328ae-7085-4919-b67b-3deeb7784afc', '641d3330-8b5e-4d73-963d-99cbe7c60ce4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', '6a47123f-6028-438d-9a9e-cb9ad77f4104', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', 'a3337170-3392-413a-8d20-a441e0f70c2d', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', '18a5ab32-7d92-4063-8a40-e4859ef31db7', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', '32aa58a7-b3c2-4cf5-9be3-487ccd16aa85', true);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', 'ea3fea9a-40e1-40fb-ba9c-da535647e706', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', 'c23e5d45-4fe9-467c-b1e3-dad6763cb119', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', '8ba7e175-1d33-4501-b2f5-9f924b5487cd', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', '641d3330-8b5e-4d73-963d-99cbe7c60ce4', false);
INSERT INTO public.client_scope_client (client_id, scope_id, default_scope) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', '6a47123f-6028-438d-9a9e-cb9ad77f4104', true);


--
-- TOC entry 3902 (class 0 OID 17328)
-- Dependencies: 261
-- Data for Name: client_scope_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', 'a2b82d25-8314-4e4f-bf55-7a152eb6ce62');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('8ba7e175-1d33-4501-b2f5-9f924b5487cd', '5383feb3-2b9a-485c-94a6-f55ec0007667');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('418e9be3-8b8a-4542-8b9f-f965312bb7cc', 'c53d2300-11db-4f62-aa7d-0d18da435e1b');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('418e9be3-8b8a-4542-8b9f-f965312bb7cc', 'b8d2a933-8832-4b7c-8b97-6a947fd8a1cf');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', 'c57117e1-dbbf-406c-8e4e-1d100911514f');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', 'c53d2300-11db-4f62-aa7d-0d18da435e1b');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', 'edd949da-dfb2-4f8a-9fe5-a8d52b4e36b8');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('6a47123f-6028-438d-9a9e-cb9ad77f4104', 'b8d2a933-8832-4b7c-8b97-6a947fd8a1cf');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('418e9be3-8b8a-4542-8b9f-f965312bb7cc', 'c57117e1-dbbf-406c-8e4e-1d100911514f');
INSERT INTO public.client_scope_role_mapping (scope_id, role_id) VALUES ('418e9be3-8b8a-4542-8b9f-f965312bb7cc', 'edd949da-dfb2-4f8a-9fe5-a8d52b4e36b8');


--
-- TOC entry 3844 (class 0 OID 16413)
-- Dependencies: 203
-- Data for Name: client_session; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3889 (class 0 OID 17090)
-- Dependencies: 248
-- Data for Name: client_session_auth_status; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3867 (class 0 OID 16781)
-- Dependencies: 226
-- Data for Name: client_session_note; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3881 (class 0 OID 16968)
-- Dependencies: 240
-- Data for Name: client_session_prot_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3845 (class 0 OID 16419)
-- Dependencies: 204
-- Data for Name: client_session_role; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3890 (class 0 OID 17171)
-- Dependencies: 249
-- Data for Name: client_user_session_note; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3920 (class 0 OID 17574)
-- Dependencies: 279
-- Data for Name: component; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('413fd099-ccc2-40f7-ad76-5bb87c82d97f', 'Trusted Hosts', 'master', 'trusted-hosts', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('47710b14-24a6-4c76-af4d-ff5cd0c3cd73', 'Consent Required', 'master', 'consent-required', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('be4ce274-1d6a-4bc3-a692-a46a27e5c8e7', 'Full Scope Disabled', 'master', 'scope', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('e5319ac0-6658-4ad6-acaa-a27d166c5601', 'Max Clients Limit', 'master', 'max-clients', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'Allowed Protocol Mapper Types', 'master', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('06a3cc74-69a6-4d92-8836-e2018d7ad8c2', 'Allowed Client Scopes', 'master', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'Allowed Protocol Mapper Types', 'master', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'authenticated');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('50a304aa-9cc7-4e60-97d9-fbd069370cc0', 'Allowed Client Scopes', 'master', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'authenticated');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('b9b091fd-13bf-4ce3-81cc-2cad1525f2b1', 'rsa-generated', 'master', 'rsa-generated', 'org.keycloak.keys.KeyProvider', 'master', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('e261d391-9470-4e6d-b48c-a045fedab400', 'rsa-enc-generated', 'master', 'rsa-enc-generated', 'org.keycloak.keys.KeyProvider', 'master', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('b5165fbd-4b6d-4a68-8b54-c584b56b736f', 'hmac-generated', 'master', 'hmac-generated', 'org.keycloak.keys.KeyProvider', 'master', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('d3ad9cf8-1b74-4888-8554-66bd0a72abad', 'aes-generated', 'master', 'aes-generated', 'org.keycloak.keys.KeyProvider', 'master', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('328a5ea9-daab-4e0a-9874-c7a77896836f', 'rsa-generated', 'university', 'rsa-generated', 'org.keycloak.keys.KeyProvider', 'university', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('a528277a-dd87-4a77-8bfb-ab9a4be67626', 'rsa-enc-generated', 'university', 'rsa-enc-generated', 'org.keycloak.keys.KeyProvider', 'university', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('2c2c6e81-fc6f-4988-a423-7903ba18c54e', 'hmac-generated', 'university', 'hmac-generated', 'org.keycloak.keys.KeyProvider', 'university', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('8eb583d5-1e21-4382-b872-449cedfefec9', 'aes-generated', 'university', 'aes-generated', 'org.keycloak.keys.KeyProvider', 'university', NULL);
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('ec7eebe8-aa04-44a6-805c-a0f33907f777', 'Trusted Hosts', 'university', 'trusted-hosts', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('8be66b4b-54dc-4e39-8683-ba91c17ac6f8', 'Consent Required', 'university', 'consent-required', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('81c81990-f5ed-48fb-ac76-1fb714be712c', 'Full Scope Disabled', 'university', 'scope', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('51116fd0-4402-4eb0-a046-18bb75ad906d', 'Max Clients Limit', 'university', 'max-clients', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'Allowed Protocol Mapper Types', 'university', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('29f03448-b150-473f-bf9a-47d13940d9ab', 'Allowed Client Scopes', 'university', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'anonymous');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('6fee041f-8807-4a55-b413-eef9428e13ff', 'Allowed Protocol Mapper Types', 'university', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'authenticated');
INSERT INTO public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) VALUES ('e7f75c94-dadb-4b68-bba5-972b094b5ac8', 'Allowed Client Scopes', 'university', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'university', 'authenticated');


--
-- TOC entry 3919 (class 0 OID 17568)
-- Dependencies: 278
-- Data for Name: component_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.component_config (id, component_id, name, value) VALUES ('04ac0cff-36a6-404d-acef-ae13941efc71', '50a304aa-9cc7-4e60-97d9-fbd069370cc0', 'allow-default-scopes', 'true');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('9f7a97b6-aa1d-4c10-b913-60805253fcd6', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('f09a208b-3b8b-467c-ab0d-0d230dcbfb02', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'oidc-address-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('80e664c3-b0cc-41e9-b2ec-6741abe6a11e', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('7ddad0bc-9ef6-43ec-a31a-d267292dfbcb', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('981f7fb5-48f9-4dba-8910-90b1194731b6', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('23c28284-52eb-4241-b7d4-34eaf8097414', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('f6e0aa95-5510-419c-b914-256315d89462', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('201703f1-0230-4525-9c80-9feb14bef6cf', '06f18f9d-5014-4e1c-8bbc-2f4f0e2d3af9', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('fc50f27e-9310-4473-b22a-e3858f3aee96', '06a3cc74-69a6-4d92-8836-e2018d7ad8c2', 'allow-default-scopes', 'true');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('97774bee-5b67-4352-94c7-b1b8bdfcb5fe', '413fd099-ccc2-40f7-ad76-5bb87c82d97f', 'host-sending-registration-request-must-match', 'true');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('ffa20bbb-a515-4a6e-b659-9bc8b659dcf3', '413fd099-ccc2-40f7-ad76-5bb87c82d97f', 'client-uris-must-match', 'true');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('8cc12b77-2ca7-4bd4-a530-09c5e4629ae5', 'e5319ac0-6658-4ad6-acaa-a27d166c5601', 'max-clients', '200');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('13e1f6a9-8919-48c8-90a5-490e2d07c434', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('b2caffa4-86a8-4d5f-bf0c-efdeb131e349', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('bd07a411-ccdb-443d-9419-945427cc49c1', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('551600d4-1419-443d-9374-799c10cdc982', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'oidc-address-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('a5f92b18-0937-40aa-b71e-9989b6141d0c', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('6ae15ca9-9cbd-4499-baf8-691116aedfde', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('6dd5d5ff-3238-4456-9b2b-6c35c046e969', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('028684f2-22db-40c6-9913-8c509f4494c6', '6a1542fb-a044-40bf-8d2d-17b47c9ea849', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('1669c0cb-f744-47ee-94f2-fb00dbf22dd3', 'e261d391-9470-4e6d-b48c-a045fedab400', 'privateKey', 'MIIEpAIBAAKCAQEAlkenxVc9ETYOLCwEx01l/Skb+dUWamYRXdm2JoZQjcIn7Va18IzUsokmkXjYP7Jed7Y8rH5kiWCpE4aGwC2GYzXuvKUP7keRhlwpkbKElhsDCl2iNe2lNyDfm5io2hW8I3vQkncjiRyTVK+dlsnknV27xlXioIBmJrHadPL6+WvVGj0vLdoE2lIKYb1EHyirlGmGJd8Wt5awBs6KnT4AzJ/16DN5747nPWC8530uaebYglv3SJ4pwTzMsTL7e/JVWwR/3C81GZaoPB03uw6j0launbxPXWs61gBpAzDOsWugOfjwrTDj+Sfsu4dP549QTHeaTxF5Usm0orh4zTMGaQIDAQABAoIBAHa7/q5IkxM6JlB4Jv5hBKfVBymJVsCSDD6eePBPRu357Rn0XKPGYXb+Ld7aH+GTyFfFQIL5Ce4Q1QV8UJMeZQOV5R5fPAsDuNLzllRrO55tkSVrfXImZVb9Pi7tmttUOOXAOba/MvBuB2ntpxEwStSa7nVx/jDZwdvowbWNFQZjFomAd11f1rOP0zkrnnxjwWu5O4iajhoUq7J9MDRuVTEYhFVZck8WNx8Jz/tyGP3WAkV7gsV7GCvadV2nCgcwavlF+U39EURB0NYVspWg3f1miW+IcNiVfJUvMf9lfIvo19/bOUy6RwTFJo4Mel+BB23xf4IKlfqOHe8UsUQ8TlECgYEA5fJIffLJESOzqzYM9wo77TPen9b4vF7N/9Bwnmlvj+Jy3IFUdimBkY7EzIhIQk8yyJe4LIxulcmwKmzXBseQ92AfUQE8gCg3TGLM7nhU6eH1AaJ1xGitoCMQhHiFaWUER5dML+WyJgnp9FFghaD8VBeLKjYwzsakbtPvYnwW8g8CgYEAp06aXwNNScL3VIGpGrZYUOZfjeuYJTSjt96tA2Q+uJXxHJOYebwVGII/O+jvpRbP/D3yerJjNHRIf+VVaV8xcHFFATFWa8PkbQjh0HPaCsqvmUtRxg+0GyaHXGkr64fnGaAwAB85MOqxWMRk4nY3qj2KlEUL+bmM/slLaL0PGAcCgYBdKzAIKCkj0q1nv7y9E954G74mUXnzCQ66igA3j0zupbgYdCZ+wg6HpatDnCx7AY/4CtFK65ObCV9hsTvmuVGZic7miXO0EZdmUdnq/cKaCT22bdb9v9QpwEtfAV489/sl7r2l4pGuK/IpVhefzTyv7eIrNk3MWlwhs6mY4+KALQKBgQCcQ2slkPTwDtxpHo1uOJ92OCVJbaF2Jps/UgiwYUG/r7Q6Z5SDJuthL82VzDOAMzVhOA6xEMpdpLpOUkmbGGEZz4GO4qEAGD/SovgedQZTDApEmDexUNt8fFjzM/vkidOMeLQ8eO02xg2Mhipf5eCJ7d7wKylLPkX1dtcq5WIiowKBgQCCIxSivQ80HNHzlwkrsrNflS41uCmc851QZSa5TljGBFjZZH4wcYclqRDmdBF/KE8xg2Tfyew2A4011JLcIRTkAO4tQjV6xj+u8i3nE85PWTvGr98vGoe53N4jqaQrdAnAyQaJAlmyKSkD+fV47MCGpVstt+XPnvwbLL0DyhHW/g==');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('0dc5e8e8-0fda-4b88-8675-977c13b6ccc0', 'e261d391-9470-4e6d-b48c-a045fedab400', 'certificate', 'MIICmzCCAYMCBgF+i3CYhTANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjIwMTI0MDkzMzM5WhcNMzIwMTI0MDkzNTE5WjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCWR6fFVz0RNg4sLATHTWX9KRv51RZqZhFd2bYmhlCNwiftVrXwjNSyiSaReNg/sl53tjysfmSJYKkThobALYZjNe68pQ/uR5GGXCmRsoSWGwMKXaI17aU3IN+bmKjaFbwje9CSdyOJHJNUr52WyeSdXbvGVeKggGYmsdp08vr5a9UaPS8t2gTaUgphvUQfKKuUaYYl3xa3lrAGzoqdPgDMn/XoM3nvjuc9YLznfS5p5tiCW/dIninBPMyxMvt78lVbBH/cLzUZlqg8HTe7DqPSVq6dvE9dazrWAGkDMM6xa6A5+PCtMOP5J+y7h0/nj1BMd5pPEXlSybSiuHjNMwZpAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAHAdocAplsUM9xkL6f6rKDHVf2kdeN/eCwmN6vIO6kKhvitnQpOByxyYDSXdNiFBoLfXDHxi/nbzpsLUmNJZmPQw4mB1cAPmjAkwE9g8fwBpC3fll6qxOvhz3kuuxsJFuGI84Ew4b4rCYbXBipo30JfE30rhRqU8LMJ1mhPpl9zOJgJvieQrfApp5ej9qrLMfIO8DnpvopPNtIZ7q474rQ1fqFUfuQReMj6Iu31eR6cJsvCPrYH6HPolz0fn6A5HxA+Dy2kNGXnaNk4R7/44uZ30JxjsML0DglpMn6wzU9Jdc68dpgv1Mxe0mpHwFvH0RP1O9+x3KiN7Gwtk9645DfQ=');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('bf87cb78-985e-492a-9f1b-57f8cd6d3604', 'e261d391-9470-4e6d-b48c-a045fedab400', 'keyUse', 'ENC');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('090aa170-e074-44f3-9dec-e91214ccc813', 'e261d391-9470-4e6d-b48c-a045fedab400', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('1f902b08-7d94-4db2-92c3-8c953f006f12', 'e261d391-9470-4e6d-b48c-a045fedab400', 'algorithm', 'RSA-OAEP');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('1b65dec5-6f6b-4ef0-8f1b-a6dd51d0fad5', 'd3ad9cf8-1b74-4888-8554-66bd0a72abad', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('1c37cf60-44e5-4769-b6fa-60d0299b240b', 'd3ad9cf8-1b74-4888-8554-66bd0a72abad', 'secret', 'AJIxG_b7CD3uHGR9PPEvoA');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('6541cf55-79ad-4912-9120-de4d6e2fb17f', 'd3ad9cf8-1b74-4888-8554-66bd0a72abad', 'kid', 'f8b82b25-6a5e-4b99-a3db-65319dfdd44e');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('3c3ad22a-b812-49ff-aab2-184118988af8', 'b5165fbd-4b6d-4a68-8b54-c584b56b736f', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('c787af22-58b0-4b02-943f-47aefc2facbe', 'b5165fbd-4b6d-4a68-8b54-c584b56b736f', 'kid', '69344e61-5efb-465a-9f35-823a3b337981');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('71215d42-32b0-40cc-8eae-01ac5133abc0', 'b5165fbd-4b6d-4a68-8b54-c584b56b736f', 'secret', 'OG-ixBauAYw_mP-Y2P5TMRy8A0VXrYxsTYSz5_o2bxjZWbevBGyYGQvOawilhxMMqhqcKVf5Tasu0FFdWPtQRg');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('159447f9-f7cf-4440-be98-06e61b9e53b3', 'b5165fbd-4b6d-4a68-8b54-c584b56b736f', 'algorithm', 'HS256');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('d2584d07-bc39-45f2-a85f-ccee5bd977ee', 'b9b091fd-13bf-4ce3-81cc-2cad1525f2b1', 'certificate', 'MIICmzCCAYMCBgF+i3CYGTANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjIwMTI0MDkzMzM5WhcNMzIwMTI0MDkzNTE5WjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCGNx6k2PShRjFGV9lnFJ2U7wVGoTGIKZseEQJwQgChyX5JShE0lHnELzlxEJxACsyI15Hs7FDHFhchw5Q9NAwzQuA+wgQLffudrdNIp2NKMCjRyocUUmKcQq0UAZePzXzsFVe4u52rKRnh9qsVAyvMd8TqRogqo6GcqAXsPov/yZMa/KTPJc7qmIQ3RcZ2yus5rudNgYWkUgnYJvp7MWK39gqWCRDpTk53reNlzVThjhlS9O46LiRkfYHRUMwBPyJ78LcJbrpClQsk0A/U8opg8X7hd5C2WjKt1sXOxFj0Enqqhbf53ZwL8Q47qWIrFgjt57Dqxjhq4nT1RQcdZiy7AgMBAAEwDQYJKoZIhvcNAQELBQADggEBAEx1f5cfwpWtqwsLhnMCLnRg9hA9JG+LsOCseOMc59ThpHY4sakLvwAHlVGAzxdw/U1An/iT3SEg8lgigBJ3Pl9GUkV8d66qDmrapO7WAvwMnB4ack/8mS7/qq/wR6WBOwvgGnflEhI3RAboEDqjPJ1DyQeTTRJ7nhY8/I3G7bvSpZZb0i2KInVfmzxpzCHagg8LGvxnQartNRQItf5re9twNW/wCFkym60biuKC8TvimdXVfMbhWqxz9CbIP7FeIkj1kFX5AE3Y1Mhg0Cc9+wAzbvTCSDMDTxNsdPryVRGw6AhT7Ak3Q+9jGENNnTyTaGT+MpQtFjCa358ajoCd7FE=');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('6737f3ce-e7cb-49c9-bdc3-2d6165ffea13', 'b9b091fd-13bf-4ce3-81cc-2cad1525f2b1', 'keyUse', 'SIG');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('78e5029f-b0a7-4174-bbe5-6a8277d6201a', 'b9b091fd-13bf-4ce3-81cc-2cad1525f2b1', 'privateKey', 'MIIEogIBAAKCAQEAhjcepNj0oUYxRlfZZxSdlO8FRqExiCmbHhECcEIAocl+SUoRNJR5xC85cRCcQArMiNeR7OxQxxYXIcOUPTQMM0LgPsIEC337na3TSKdjSjAo0cqHFFJinEKtFAGXj8187BVXuLudqykZ4farFQMrzHfE6kaIKqOhnKgF7D6L/8mTGvykzyXO6piEN0XGdsrrOa7nTYGFpFIJ2Cb6ezFit/YKlgkQ6U5Od63jZc1U4Y4ZUvTuOi4kZH2B0VDMAT8ie/C3CW66QpULJNAP1PKKYPF+4XeQtloyrdbFzsRY9BJ6qoW3+d2cC/EOO6liKxYI7eew6sY4auJ09UUHHWYsuwIDAQABAoIBAFvf02xIz3yujgB3Fm0Hoa9XxybIJR3uTS7RufeaVtBpG7eTqh9yG8B+CzYohq0YN1bdKhki01DVAFSuiwUyYb+dYd/T8+zh+qG/Ych0PPWF7gteNgVlN/d7dPsi89sOcRsyqO/eDloiMoOJ1Sb3Gr5GplcwnPmDNZgFNLi5QCCBnz+dyF/e52WmvCbpVHY6fYbIaJORlsx7P3AgvPrijMabbBHcjq/nWG2FA0ubxg2XRnJJNu0rYbyvWyhlevW8coZKaTv4BlQar+niVHYiOiBtVb9lpdBbIZVu8q2fq5rIhyNhp/rO9Z5aTgkfEOqACDJ+eEwcuzEmzTy5QWWAQaECgYEAySfiDzwCouY+7hvCr4Wk3DODDNBKpLt8uHKxCKFAWLRixqXB4XeYmAv0svlRX+GhW/1ruijq6Hs8UGKEnHbNAeWioEoNAZHKogU5mdnIAJaohuJD4HwlFpJBMulCv/Ky9f/YrlZRnObzyBc4F07iHDX0oqf5KClWwKMiNonqwlkCgYEAqs75/UvJ8Iq2OZj22l2QipJSES3lBiYkd0095tQocEzSQpK5VTHsckUpriS0AdHjs1DzL9qa5tzdlVGfdnPuiVc+fYR5I4ZdE3102SfPG+d7Y68E6XN1wIoxoalQMjgkbF6SLPVwiPsDevbGE+YRyPtt69BZC5DpA1WOmKkcfTMCgYAQGUYJTBc17FFrAwPAwmRgTysU0nK23l5r8Jnkg+s/bihdl5R9s/GdcY+C51sw3kDouM5vVqj+NCaulEPitctQdI1gYKzLzFw4PUzP2FMeYPBZ4y/lfmoFdW0MYwi5jfXupCw2P6hGc2FdSSZvDxwff931SpYxNaXAww2Q6dbE+QKBgCspAaIvdGeDpWgIbtferJdXQcV1g1l6adx7eBjLoyw9PSP1Tz9nzgdR0qZeP93120OEQO7mnh2X0aXZaiHUSYIKuY+d2b5QRVSXbt8wX/T9GQCnSOZ0dlygjsn6rnkCFrILc4RIwJbcVpTLpmGA0R2n6q6bhuSVSUu+YrnQgZJhAoGAPF9I6K5oXKzOCbv8ZJm+hhsocDWhSDbIrs4wLO7f4BziP5Vea8d1RaYO6D1q/JSQAqk/rjVEOCf1zn60/dkXHgFG79kmY1zE+bTSGv+Ke1p/Jx9TKdPMslsZ5QFJ0s8PronIu0fF+AGgsTYZ7x5sDkqbCBL24qt7zABFxpq2vMQ=');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('b839b29a-4d0a-4482-9aed-e9f10afc3aac', 'b9b091fd-13bf-4ce3-81cc-2cad1525f2b1', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('598d0e2b-f3d6-42f6-86c7-52eeb87695ba', '8eb583d5-1e21-4382-b872-449cedfefec9', 'kid', 'a50dafa4-d873-4b76-be3c-022f6ab2d626');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('57f2aa90-60ee-43e7-bcdc-8f7ebd3df49b', '8eb583d5-1e21-4382-b872-449cedfefec9', 'secret', 'cpX1SiEa7tVKMNdkuobHKA');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('222095de-93a3-4dd7-87d8-9784b554d807', '8eb583d5-1e21-4382-b872-449cedfefec9', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('31b0df2f-065e-4762-b5d2-d2c43392e176', '328a5ea9-daab-4e0a-9874-c7a77896836f', 'keyUse', 'SIG');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('72db57e8-df69-441c-8f14-03ae774f4ac5', '328a5ea9-daab-4e0a-9874-c7a77896836f', 'certificate', 'MIICozCCAYsCBgF+i3txWzANBgkqhkiG9w0BAQsFADAVMRMwEQYDVQQDDAp1bml2ZXJzaXR5MB4XDTIyMDEyNDA5NDUzMFoXDTMyMDEyNDA5NDcxMFowFTETMBEGA1UEAwwKdW5pdmVyc2l0eTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAIrBhXb2kPMLc77SLmq5NslVHALHRhMmXSyxSbaC+xX/jls9RGBfAMM2u7ME+z6DNfxjQTvkumYJFIRZFpGRytN17qfxBN5IyzDp1udrMRTxkec7sjxsv6JHVjeQ4rb5mt8pQE92Ds1G6okGQnVi78kiQwdEGfZDkLhRAgnbuycyjBGjqVQXAQr2UHAojFlJ70x6kYWkcc6S84wIjenQEjkECa+ejaM+CV3LOsEEcADOWVxZ4UkGNZ4mxoeyG2LeGMeb+R9ztE3v+cNnPwYau261wX303D+57djU2slO3eQyWxODeEAxpZdeoXbqrbzrex6BCFiuiLXz9GQOm4Zy9ikCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAa7GZvkLhSfruUK7TGMXVwjAOa9yutWX4OnsD/Jz20gwkrSWQO3Sspl97dsdIwnxhIBYz6vJCj/UCcBhf0HI3X/bcnSfVDcMO9Psr+05ZkYWIKmGwULW7JIlkjNRoekmNszfm0HBSulqev6bCWpYw6s4eoII15wHPR2Eh/Fm0rqZfAhJb6Kop9gwJQWgiuXaPoRVFOPY41WXLutGFXbkXtY5zQ6IlIdYFWmfP4ubSpbPWTN8J+GcjTkQBeO+fvyZfdCTRc4gSjC9RWff7RufuxPiB9DyFpnAJJoOOzIAF5XlapkCxLfVuhRZJ1n451aEY8I6AwStMXBsKMtawtqTdVQ==');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('00792831-32a9-4c65-8341-d3255ae10391', '328a5ea9-daab-4e0a-9874-c7a77896836f', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('16ee6e5f-5072-442f-a1d8-1ce578a9e6e6', '328a5ea9-daab-4e0a-9874-c7a77896836f', 'privateKey', 'MIIEowIBAAKCAQEAisGFdvaQ8wtzvtIuark2yVUcAsdGEyZdLLFJtoL7Ff+OWz1EYF8Awza7swT7PoM1/GNBO+S6ZgkUhFkWkZHK03Xup/EE3kjLMOnW52sxFPGR5zuyPGy/okdWN5Ditvma3ylAT3YOzUbqiQZCdWLvySJDB0QZ9kOQuFECCdu7JzKMEaOpVBcBCvZQcCiMWUnvTHqRhaRxzpLzjAiN6dASOQQJr56Noz4JXcs6wQRwAM5ZXFnhSQY1nibGh7IbYt4Yx5v5H3O0Te/5w2c/Bhq7brXBffTcP7nt2NTayU7d5DJbE4N4QDGll16hduqtvOt7HoEIWK6ItfP0ZA6bhnL2KQIDAQABAoIBAElG3ebvW6ke2Sdi9CRkX6x/h+Vnu3gNgJNDpTv/e/4oEdPZTSzfvxVY6FMoRE7FGFOLTz4EEvQjw3QJYEPMDKMT2s9SF1IUe0n/lABuuWLoGDdXbU8/1wwqRDvgBfq/YqyLqmzMWhDKbL2kUqCPeTZtI5zIuDU3hUvxa/Qs2G9gGUMz5H0uSIfcabxVhC9uR4MXGEjjeeo2o5dTzR26wmLSvB9H8RLSSPZaCirSbt1gj5ZN40C+ByOP1EY1D3hio0krUz6GXAqMkLdN1QY/Baw04+wysAVoPpDFVoVr2U6p/GuY4dh1aQ4e0f5PhTLPbCXcSKAX7dDchMoUvYRBZiECgYEAv+3RU6ydUHYOUTrbzRIM6zw5QzvC3P9gtId1ZIy5F/lIis9619YTCrZxAextqz8vdUBUqLHHneDAVrgDmcV3deSse6ipDl12u1b/6M0da191jtbMZ4i0ehUsfyLseeqYmhzhhEemjh6c2Lhc06jbhWb3onvuOZhnptHYBogKq10CgYEAuROOHBH/O2boEuhSBktghyNGYnO/kLbA4ryfyrgGmbIIZkntPPpd119cOuUnMvBm1OkEP3rYslOeveA1+NMC7l0ndx5nZR9om0/v5HImoZ8RcyEgDTgyBmOaz/6YP3nSAU2UI9ytlCdwQb8AYtdNG9R/v76J+TgR9s2oGEcnlT0CgYEAo4reJ9KaagluwcDJssCCyZ+Tm7q8nNvvZ2VVnsxanBcKvV12a5w6tZIoUV9JQ/afVeXvPZ/fXKQ6wKokR0+AL7EfWG+rPlZd6dyES7ccLnY1EfXuTvqp7q1EMUFtB6XjSs75S2JShh+HvxO55ZGpHM9Ui9vWbrVy/S8qSMZ7cPECgYAC2uVfU3u/e9fqqsDRLgqAKzAjxqtW3SrelJBWfT5E0rVEdrF9NlOKRgNTOUPAimrbr/iLziZXoeiZ/0J14zNxX+iC8uofajuf4bzOpmAoMB8ByqoG6i4DYTf7K29H02ZE9OBrKTVYSr91lC+6Vp3u5vlNiO/x80Z7qzKDpV1b9QKBgGGxjCDiVyzz928O3Pe9YCI3e7zgcwXyvhSnjHXQ1gxL71kgQ06S2v0jSXaL0qZwZin9M/9FCbLt1us5IY+R+UheQYXZ33bLBFzWkhimOzMsKHXT7xMyd0mofvaIQ34b+9PylQYUIXzGRXsaMOl8A4jasV3qrp+6lbmkXZrYlZSr');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('bbba5fe1-9db9-4707-86d4-d18d749e6a9a', '2c2c6e81-fc6f-4988-a423-7903ba18c54e', 'kid', '8590bf27-d10a-40ea-ac41-42efac6e6cc4');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('ceba5c0a-1308-47c7-a846-d3ba9014bf86', '2c2c6e81-fc6f-4988-a423-7903ba18c54e', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('f5ea2bf7-1c73-477e-ba8c-acd8408f2421', '2c2c6e81-fc6f-4988-a423-7903ba18c54e', 'secret', 'IB-fpm5SZWL6Ye3PZXKGRC3lraCfz8fvAgTXNsBI8XbC8n9fBz4_rVeeJf1gCIMzzwSA17XgqaRVY295UgwD1A');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('3d3fb851-0bf6-4e42-9984-9fc6fbb298f5', '2c2c6e81-fc6f-4988-a423-7903ba18c54e', 'algorithm', 'HS256');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('b8e88626-9a8e-4715-8009-0352bb0a1933', 'a528277a-dd87-4a77-8bfb-ab9a4be67626', 'keyUse', 'ENC');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('0d9e0955-57ca-4eea-a915-5a54f2049e6c', 'a528277a-dd87-4a77-8bfb-ab9a4be67626', 'certificate', 'MIICozCCAYsCBgF+i3txhDANBgkqhkiG9w0BAQsFADAVMRMwEQYDVQQDDAp1bml2ZXJzaXR5MB4XDTIyMDEyNDA5NDUzMFoXDTMyMDEyNDA5NDcxMFowFTETMBEGA1UEAwwKdW5pdmVyc2l0eTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJb00DZ9DylRf/xx7q7bZjd1lgVVLdr2wmw9Snk2bDYqMtMdTrzpmqtWAORVTNHcVo6BCvVvjjMAzzvo4r9WCVCWhmaaiiSIk3NHnMYC2K80YfkiIm3P+DgrnCWyGEWp5oE7SleShFitFEqzCxq0WJOB/4+rGtf47l0r/V6C7xOP91dg1u/dnvhs1pebHR8A7t5QUSkRJNlIJuDDDhvimumQDiEnlY4Kdzl8NFqvHqK04rFXgB6JCS6uc7cg8beZS5LtqHmPYydpBALs54Za78K83u8CWJk1bYxmk9H9kN+bM6xp0vZ89Dj+LNgYOgmPLK0BvuQpDqpncugZHGIRRBcCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAFSDFhTuj/nyHllFmPb1Jp0Qm5KKiKltXMLLC9QVrsoJJKDnu8jaqvwntH7dAIA1pbi+zn1PdNigIRmduclnR0fuXfxhTkQ5nep6WhwRu/M+8Ow0bXIBgNS1tg/hApmkmFWbKew82633yU302bCaf4mT7IjA4eOofYobwu4jKPT2G3OccAtj2m9rOIxrt697I/px24BWBfjIm0B4MCmn4eltNCg4KU41zCYhQjBrAP+xW2ETwXwC83z+pNxBN/eOc+YJMU1aqoryCmYV1X6+A7TvKbNfBDyz1pKqOFXx4yrB8FxiefQ4VZqTr0suKkTqu9lT3VhW8wUt5+i5Kcjf9GQ==');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('3f63c425-b42b-40f3-a15f-51ef6c1a6c4e', 'a528277a-dd87-4a77-8bfb-ab9a4be67626', 'priority', '100');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('b7175c82-3a5d-43ac-8ca0-ee3b269f8982', 'a528277a-dd87-4a77-8bfb-ab9a4be67626', 'privateKey', 'MIIEowIBAAKCAQEAlvTQNn0PKVF//HHurttmN3WWBVUt2vbCbD1KeTZsNioy0x1OvOmaq1YA5FVM0dxWjoEK9W+OMwDPO+jiv1YJUJaGZpqKJIiTc0ecxgLYrzRh+SIibc/4OCucJbIYRanmgTtKV5KEWK0USrMLGrRYk4H/j6sa1/juXSv9XoLvE4/3V2DW792e+GzWl5sdHwDu3lBRKREk2Ugm4MMOG+Ka6ZAOISeVjgp3OXw0Wq8eorTisVeAHokJLq5ztyDxt5lLku2oeY9jJ2kEAuznhlrvwrze7wJYmTVtjGaT0f2Q35szrGnS9nz0OP4s2Bg6CY8srQG+5CkOqmdy6BkcYhFEFwIDAQABAoIBABwVLybz3P4yyzyLdXvSFRH8LgyP7BfwD/tS66fhGAB7mTjtGtjKHJwDQ+c2dzQTaYbZOtCuECYs4GCk0ooF13XGivpLhaeGQOgRSnkLzoPJmvBeCUmkhujsNABPQXS0Rs2D2jCwcS6OBFhhNo+hPVkztfdkPXowNSfgJyKN+SwKMvvpIomeFrAIY85Z8pzJ++Io2kdDiyYZ1+PePf+0R+TV6O+x4RnV06hObNBybREnwKx5Al9z0NDXTiCf7+Rj9x9Kka0vkazwfYHgbtBoRAmGmkRxfcPHKY6Pzm+dGzSDqGmrMVPAxhywLk+B4GwWw/lC0s5auwppHoVsJm0rmwECgYEA1IhShoZXcZO4Vx/QoM2bAhTYW/wXu82b3DEsSmUaPktyMSmVJfOPdwwP22LtdSE5LuyZlkYS8qMKOpoEzjCWzUPDn+vSIgFcd/W5QVVM12ZLebFwjstZBVtUfBy7PwTVomdXukf/gkVbNZbwML+9GPdFox8vj/+xJDIgVE1IcYMCgYEAtdSDQpSDe7YGuh55Kd3mai4oExLQtXEHCubkquVu1mEEtozYomxPCvegUEGu5pMwEsGD0SSYA6JQ7VtNg4U/u2KdPybo8/OcHiTDFrhMJ3EMh0a6upNNAka2GmhrFFcZnxmoCevtHKvyBV8Nf0CZTHx6reLtM/npL39SkcvWwt0CgYBcfes0QjjB4+CggecAZBomYL374gzOHV/MBtZYlgFCxQCwJoGTH5j63RP1jOr8Q3YoVq1Mebh7QbsM38Rhm0G1mN0EttMfG2qLhjOWHDIxt4H3NuJeMl7Odv2gebx/Y3I5mDHUjsrCfb6A5uwmuvrnZ401vTesukD0GKB7L5rsUwKBgEqqmbwdgGCsvtZu7RJkLNotWgd7EnlvkJTJe90xmfIupODRtaP9FfhK92n9jvTgPD13ecnIXSQg41qUw5/ed+1a8XedYsvKnbFDJ1mNFLidk6d5Q/SZyGXG48C0YERaAAt1Xau1VgkQErJh9gjrXggqlbYSGZXeagM3JZD5Q5eBAoGBAMBJsIRG92uA3lNfMBNrF963cg0AfweRPIMt/yr458kHxrQUmVKpDoEmB4t/UcyoxbWWVjprS3R07gpXUOxiZ/WoxLxlijGk/7lYvF13Llpk6F88vLI9ttWPKiuKxeQCvbfNUnN6ZHbmNTLkVRnnet60/QaIHS1I/7tPu6u9bPNo');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('964ca5e4-31e0-4aef-b234-2c8e9e42f78e', 'a528277a-dd87-4a77-8bfb-ab9a4be67626', 'algorithm', 'RSA-OAEP');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('8e94501b-bb5a-4e5c-869c-89153a7e9145', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'oidc-address-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('3c20fa7d-2405-4d9c-9a83-22e73bf6855a', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('ed06bc86-b93c-42e7-bb5f-9b11353af028', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('4e0d769a-0a14-45a3-958d-7b5908f7020b', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('6f90b6fb-53ac-4cab-89bc-51a60b35c9f2', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('21eb7741-8137-46b7-860d-58cdf79f95b6', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('30d2be65-30bb-471b-b4a4-1120272759de', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('a52643ca-23a8-450d-9b06-b98c292cc7ec', '66008ca2-25bb-40f5-a6ab-f45fd8ca7765', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('7c3cfa59-6059-4a7a-9bb8-e88b285f4188', '29f03448-b150-473f-bf9a-47d13940d9ab', 'allow-default-scopes', 'true');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('d759bd38-d398-4c9f-8020-ff6d6d8ac761', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('7ee3ea96-06ee-49ad-a926-979007fa88c6', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('4f27a451-07f8-4fea-8d51-37331f9c88de', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'oidc-address-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('4371d62d-4bc3-41b1-997f-e9c3ce2afacb', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('22bd3a5b-f06d-49bd-870c-93d36fa1b753', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('5e4cfb77-6d56-4421-987d-8120846efe34', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('ba8d8134-b838-4204-8eb5-61a6b63bad9f', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('69fe7981-9da8-498c-97e4-a5d2ef809df6', '6fee041f-8807-4a55-b413-eef9428e13ff', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('5f2ffb67-1cd4-4964-a962-251c171542be', '51116fd0-4402-4eb0-a046-18bb75ad906d', 'max-clients', '200');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('23d9a80f-77ba-4c19-94a9-3b5b47ab41f5', 'ec7eebe8-aa04-44a6-805c-a0f33907f777', 'client-uris-must-match', 'true');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('effb581c-387e-4977-b273-aade00813045', 'ec7eebe8-aa04-44a6-805c-a0f33907f777', 'host-sending-registration-request-must-match', 'true');
INSERT INTO public.component_config (id, component_id, name, value) VALUES ('d0341b38-afe3-4963-81c1-6f896a633ac1', 'e7f75c94-dadb-4b68-bba5-972b094b5ac8', 'allow-default-scopes', 'true');


--
-- TOC entry 3846 (class 0 OID 16422)
-- Dependencies: 205
-- Data for Name: composite_role; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '658f6e15-2f2d-4603-b934-a824230f46f3');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '2eaf7071-2108-4257-b084-7c8b92227c43');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '086b6804-1b83-4e99-a92b-ef075dac19a8');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '044d2ee4-5102-4ea4-8f41-c7d5417fa34b');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'fcf42468-fe3f-4c17-bdb0-4eaa7469e166');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '971ed0c8-7ad9-4d17-915d-2cac20559ce9');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '6b18111e-9eec-481a-a857-a5fb2a46d920');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '3ac1a25b-d51e-4d07-a005-80de4fbc6e53');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '9798391f-d21f-4b1a-a580-a64991787dae');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '22fafafa-df01-4195-8610-18aae4526919');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '07331812-fd59-43b3-9e80-d6ac5095e9c8');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'cf8e1d41-c2b1-4f7b-8417-0bb43f784b6e');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'e277078d-1182-4e3e-90f3-d1cd6a4d4cae');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'cdd3818a-9882-4a1f-83cc-a7c243471d03');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '10709f9b-6487-495e-91da-ef00b7702d9f');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '1aa9da8b-1bbd-44d8-9333-1a0d4c2fc262');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '9ee4b552-dc41-44ff-9002-c78a781dc4f4');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'dbb1fd86-cbba-4897-897b-7ee66ffab456');
INSERT INTO public.composite_role (composite, child_role) VALUES ('fcf42468-fe3f-4c17-bdb0-4eaa7469e166', '1aa9da8b-1bbd-44d8-9333-1a0d4c2fc262');
INSERT INTO public.composite_role (composite, child_role) VALUES ('044d2ee4-5102-4ea4-8f41-c7d5417fa34b', 'dbb1fd86-cbba-4897-897b-7ee66ffab456');
INSERT INTO public.composite_role (composite, child_role) VALUES ('044d2ee4-5102-4ea4-8f41-c7d5417fa34b', '10709f9b-6487-495e-91da-ef00b7702d9f');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d58f3db5-7572-41c6-b871-530e48d3ab60', 'f00e5ad7-ea36-4433-99f6-e52856a9e824');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d58f3db5-7572-41c6-b871-530e48d3ab60', '62bd7fab-4794-4f6c-a5de-946763965c3b');
INSERT INTO public.composite_role (composite, child_role) VALUES ('62bd7fab-4794-4f6c-a5de-946763965c3b', '2c8c5e12-5522-487a-980f-d45cf668a2ea');
INSERT INTO public.composite_role (composite, child_role) VALUES ('190f5e7f-954f-4f72-9519-96044c4f64d6', '3813c239-f36a-461d-9b39-4154261993b3');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '247b338a-9a45-45ff-b962-2c0c9daa5908');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d58f3db5-7572-41c6-b871-530e48d3ab60', 'a2b82d25-8314-4e4f-bf55-7a152eb6ce62');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d58f3db5-7572-41c6-b871-530e48d3ab60', '58232c19-3ca8-49bf-9262-19547bf72864');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'fce01f44-4fc5-40d8-a341-2c33d6a0b9e6');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'bc8ccc9b-c507-4a28-a0ba-b06648f2c155');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'f8cc984e-0088-472e-83f6-fca0a5e6392b');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '2c10980d-a45a-465f-a5c8-79185070ec04');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'a5895c2c-6c9c-40ab-aff7-c7d114438d6c');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'cabdaa89-998f-4c56-9c98-b62491e582f8');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'bc8668cc-e3cb-47f1-be2f-dfc417bb3b75');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '67859da9-37d7-4673-928d-611c8177e71a');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'bb3435ce-e19d-45fc-8773-a91c8b2bd635');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '56e049c6-cf93-47bc-af49-877f0c1c9945');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'cf871e78-2716-4c81-9ea5-da33841779a3');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'c29cc501-6b1b-4252-bdc1-60d9a7a1c325');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'ec77e898-bb39-4e15-89f4-23f43287af8e');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'bb0472bb-594e-4583-8b88-2f6970109bae');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'e4188e1a-d947-431c-81a4-a2b876f0e4ce');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '3042ec43-5d7f-4e51-b353-08f6db15d78f');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '85980e95-1b7d-4751-9b09-fe95f64f2bdd');
INSERT INTO public.composite_role (composite, child_role) VALUES ('2c10980d-a45a-465f-a5c8-79185070ec04', 'e4188e1a-d947-431c-81a4-a2b876f0e4ce');
INSERT INTO public.composite_role (composite, child_role) VALUES ('f8cc984e-0088-472e-83f6-fca0a5e6392b', 'bb0472bb-594e-4583-8b88-2f6970109bae');
INSERT INTO public.composite_role (composite, child_role) VALUES ('f8cc984e-0088-472e-83f6-fca0a5e6392b', '85980e95-1b7d-4751-9b09-fe95f64f2bdd');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '5ad18a5f-9f5e-4f19-91e6-443e45c70420');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '269fbdce-2d82-4e92-aec1-5fbcb274f58a');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', 'b1f73988-1403-454a-a88e-97316009ad66');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '8c5454ea-cacd-4410-950e-f4dd2d991bb6');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '2581c79c-ac67-409e-8ff2-5437e1f32958');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '34a46acd-9356-4386-b0bf-c6bb7d50ea5c');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '76b2d949-dd8a-4850-bf9c-2d5057d8bba7');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', 'a3e4be50-f930-4a91-afbb-0a2768a8c14b');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', 'c1e13e53-09a1-4368-b0e0-8a61c4d9b9b5');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', 'aa58067d-617a-411c-a48d-0b7e86bbf6e5');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', 'b7d2bbcc-8bfb-4dd9-97b5-708e9646a3c5');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', 'a372e2b7-19bd-4883-b33f-994b5c914bb4');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '1d1c6f09-ae74-4b37-8109-ffcbad9527ab');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', 'f5d7b9d9-4590-4ee9-b317-9021908eec69');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '5078ce17-9aef-45b6-adf8-61d7d40248d6');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '7aa83dcd-e332-409d-81c0-42ebd529338a');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '57835ab4-1993-4bdc-ab6a-83235dd5940b');
INSERT INTO public.composite_role (composite, child_role) VALUES ('8c5454ea-cacd-4410-950e-f4dd2d991bb6', '5078ce17-9aef-45b6-adf8-61d7d40248d6');
INSERT INTO public.composite_role (composite, child_role) VALUES ('b1f73988-1403-454a-a88e-97316009ad66', 'f5d7b9d9-4590-4ee9-b317-9021908eec69');
INSERT INTO public.composite_role (composite, child_role) VALUES ('b1f73988-1403-454a-a88e-97316009ad66', '57835ab4-1993-4bdc-ab6a-83235dd5940b');
INSERT INTO public.composite_role (composite, child_role) VALUES ('0d4b0f93-41a0-4794-871e-8fa2cd402992', '238eb159-1108-4290-9540-ce186a0e75a5');
INSERT INTO public.composite_role (composite, child_role) VALUES ('0d4b0f93-41a0-4794-871e-8fa2cd402992', '197755ea-6368-4aca-a626-813a9d720e11');
INSERT INTO public.composite_role (composite, child_role) VALUES ('197755ea-6368-4aca-a626-813a9d720e11', 'dcf926a0-ad05-4b11-bb40-7f6e08be28ed');
INSERT INTO public.composite_role (composite, child_role) VALUES ('a830571b-918e-40ed-8ed8-e70dfbbd0615', '052a88a8-d34e-4a34-8989-f5dbf1b937b9');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '368b145e-b8f6-4506-8497-c7b9fc542d85');
INSERT INTO public.composite_role (composite, child_role) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '070f3c45-5544-4b43-95f2-b75f946e1530');
INSERT INTO public.composite_role (composite, child_role) VALUES ('0d4b0f93-41a0-4794-871e-8fa2cd402992', '5383feb3-2b9a-485c-94a6-f55ec0007667');
INSERT INTO public.composite_role (composite, child_role) VALUES ('0d4b0f93-41a0-4794-871e-8fa2cd402992', '610dc02e-f670-4ba5-a2e8-83144e259cde');


--
-- TOC entry 3847 (class 0 OID 16425)
-- Dependencies: 206
-- Data for Name: credential; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.credential (id, salt, type, user_id, created_date, user_label, secret_data, credential_data, priority) VALUES ('0fd5bf43-5272-4f33-acf1-644b7c6622a1', NULL, 'password', '0032b116-627c-4945-b66b-2e61095f9e1b', 1643016919544, NULL, '{"value":"KF4FySSQfct3b0xtiWLOY58UdHvJQShMEoVmsKfAE0zXAqJarElxvcD2vs5Vt+RMl1Fhi2YdbPsOPXH6ixlU4w==","salt":"EBpfIw2LZ2g+StPz6WwamA==","additionalParameters":{}}', '{"hashIterations":27500,"algorithm":"pbkdf2-sha256","additionalParameters":{}}', 10);
INSERT INTO public.credential (id, salt, type, user_id, created_date, user_label, secret_data, credential_data, priority) VALUES ('71b945f9-756a-4354-8f09-09d0e7d6f18b', NULL, 'password', '7cc2b36e-e5e5-415b-a7f4-4fc4bc1ec028', 1643018079291, NULL, '{"value":"rBh4/wtZ1RKltK7S1afeKOqIlrhEmESkY03eFZTj0ZyOnKMXlLP1A9QEftp0Dj+2g4ZAQdiilhFHGpYAYwRRmA==","salt":"r0QDH3fsVnn03KL4WYTAow==","additionalParameters":{}}', '{"hashIterations":27500,"algorithm":"pbkdf2-sha256","additionalParameters":{}}', 10);
INSERT INTO public.credential (id, salt, type, user_id, created_date, user_label, secret_data, credential_data, priority) VALUES ('b2b03e9e-767d-4f01-aa8d-37f4883843fc', NULL, 'password', '7b482214-db2e-4ebb-9e42-cc9ba2672e00', 1643019553403, NULL, '{"value":"ovEa5ruM995AhVxmc8W5IpS980P/6Rwtk/dmmyGW8DDEO+wEFu9QJtEXZ/swStAChSF8PcQF/ANjUnZz/2iANg==","salt":"L5DZhI3BJKS/1CaqpJ2fQQ==","additionalParameters":{}}', '{"hashIterations":27500,"algorithm":"pbkdf2-sha256","additionalParameters":{}}', 10);


--
-- TOC entry 3842 (class 0 OID 16392)
-- Dependencies: 201
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.0.0.Final-KEYCLOAK-5461', 'sthorger@redhat.com', 'META-INF/jpa-changelog-1.0.0.Final.xml', '2022-01-24 09:35:12.136374', 1, 'EXECUTED', '7:4e70412f24a3f382c82183742ec79317', 'createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.0.0.Final-KEYCLOAK-5461', 'sthorger@redhat.com', 'META-INF/db2-jpa-changelog-1.0.0.Final.xml', '2022-01-24 09:35:12.147065', 2, 'MARK_RAN', '7:cb16724583e9675711801c6875114f28', 'createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.1.0.Beta1', 'sthorger@redhat.com', 'META-INF/jpa-changelog-1.1.0.Beta1.xml', '2022-01-24 09:35:12.202472', 3, 'EXECUTED', '7:0310eb8ba07cec616460794d42ade0fa', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=CLIENT_ATTRIBUTES; createTable tableName=CLIENT_SESSION_NOTE; createTable tableName=APP_NODE_REGISTRATIONS; addColumn table...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.1.0.Final', 'sthorger@redhat.com', 'META-INF/jpa-changelog-1.1.0.Final.xml', '2022-01-24 09:35:12.208308', 4, 'EXECUTED', '7:5d25857e708c3233ef4439df1f93f012', 'renameColumn newColumnName=EVENT_TIME, oldColumnName=TIME, tableName=EVENT_ENTITY', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.2.0.Beta1', 'psilva@redhat.com', 'META-INF/jpa-changelog-1.2.0.Beta1.xml', '2022-01-24 09:35:12.353033', 5, 'EXECUTED', '7:c7a54a1041d58eb3817a4a883b4d4e84', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.2.0.Beta1', 'psilva@redhat.com', 'META-INF/db2-jpa-changelog-1.2.0.Beta1.xml', '2022-01-24 09:35:12.357392', 6, 'MARK_RAN', '7:2e01012df20974c1c2a605ef8afe25b7', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.2.0.RC1', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.2.0.CR1.xml', '2022-01-24 09:35:12.474985', 7, 'EXECUTED', '7:0f08df48468428e0f30ee59a8ec01a41', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.2.0.RC1', 'bburke@redhat.com', 'META-INF/db2-jpa-changelog-1.2.0.CR1.xml', '2022-01-24 09:35:12.479922', 8, 'MARK_RAN', '7:a77ea2ad226b345e7d689d366f185c8c', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.2.0.Final', 'keycloak', 'META-INF/jpa-changelog-1.2.0.Final.xml', '2022-01-24 09:35:12.485914', 9, 'EXECUTED', '7:a3377a2059aefbf3b90ebb4c4cc8e2ab', 'update tableName=CLIENT; update tableName=CLIENT; update tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.3.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.3.0.xml', '2022-01-24 09:35:12.661523', 10, 'EXECUTED', '7:04c1dbedc2aa3e9756d1a1668e003451', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=ADMI...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.4.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.4.0.xml', '2022-01-24 09:35:12.745532', 11, 'EXECUTED', '7:36ef39ed560ad07062d956db861042ba', 'delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.4.0', 'bburke@redhat.com', 'META-INF/db2-jpa-changelog-1.4.0.xml', '2022-01-24 09:35:12.749229', 12, 'MARK_RAN', '7:d909180b2530479a716d3f9c9eaea3d7', 'delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.5.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.5.0.xml', '2022-01-24 09:35:12.771171', 13, 'EXECUTED', '7:cf12b04b79bea5152f165eb41f3955f6', 'delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.6.1_from15', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2022-01-24 09:35:12.804545', 14, 'EXECUTED', '7:7e32c8f05c755e8675764e7d5f514509', 'addColumn tableName=REALM; addColumn tableName=KEYCLOAK_ROLE; addColumn tableName=CLIENT; createTable tableName=OFFLINE_USER_SESSION; createTable tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_US_SES_PK2, tableName=...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.6.1_from16-pre', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2022-01-24 09:35:12.808401', 15, 'MARK_RAN', '7:980ba23cc0ec39cab731ce903dd01291', 'delete tableName=OFFLINE_CLIENT_SESSION; delete tableName=OFFLINE_USER_SESSION', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.6.1_from16', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2022-01-24 09:35:12.811327', 16, 'MARK_RAN', '7:2fa220758991285312eb84f3b4ff5336', 'dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_US_SES_PK, tableName=OFFLINE_USER_SESSION; dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_CL_SES_PK, tableName=OFFLINE_CLIENT_SESSION; addColumn tableName=OFFLINE_USER_SESSION; update tableName=OF...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.6.1', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2022-01-24 09:35:12.814068', 17, 'EXECUTED', '7:d41d8cd98f00b204e9800998ecf8427e', 'empty', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.7.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.7.0.xml', '2022-01-24 09:35:12.874372', 18, 'EXECUTED', '7:91ace540896df890cc00a0490ee52bbc', 'createTable tableName=KEYCLOAK_GROUP; createTable tableName=GROUP_ROLE_MAPPING; createTable tableName=GROUP_ATTRIBUTE; createTable tableName=USER_GROUP_MEMBERSHIP; createTable tableName=REALM_DEFAULT_GROUPS; addColumn tableName=IDENTITY_PROVIDER; ...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.8.0', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.8.0.xml', '2022-01-24 09:35:12.942251', 19, 'EXECUTED', '7:c31d1646dfa2618a9335c00e07f89f24', 'addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.8.0-2', 'keycloak', 'META-INF/jpa-changelog-1.8.0.xml', '2022-01-24 09:35:12.94965', 20, 'EXECUTED', '7:df8bc21027a4f7cbbb01f6344e89ce07', 'dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part1', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2022-01-24 09:35:14.418863', 45, 'EXECUTED', '7:6a48ce645a3525488a90fbf76adf3bb3', 'addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_RESOURCE; addColumn tableName=RESOURCE_SERVER_SCOPE', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.8.0', 'mposolda@redhat.com', 'META-INF/db2-jpa-changelog-1.8.0.xml', '2022-01-24 09:35:12.95313', 21, 'MARK_RAN', '7:f987971fe6b37d963bc95fee2b27f8df', 'addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.8.0-2', 'keycloak', 'META-INF/db2-jpa-changelog-1.8.0.xml', '2022-01-24 09:35:12.956524', 22, 'MARK_RAN', '7:df8bc21027a4f7cbbb01f6344e89ce07', 'dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.9.0', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.9.0.xml', '2022-01-24 09:35:13.012882', 23, 'EXECUTED', '7:ed2dc7f799d19ac452cbcda56c929e47', 'update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=REALM; update tableName=REALM; customChange; dr...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.9.1', 'keycloak', 'META-INF/jpa-changelog-1.9.1.xml', '2022-01-24 09:35:13.020654', 24, 'EXECUTED', '7:80b5db88a5dda36ece5f235be8757615', 'modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=PUBLIC_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.9.1', 'keycloak', 'META-INF/db2-jpa-changelog-1.9.1.xml', '2022-01-24 09:35:13.023806', 25, 'MARK_RAN', '7:1437310ed1305a9b93f8848f301726ce', 'modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('1.9.2', 'keycloak', 'META-INF/jpa-changelog-1.9.2.xml', '2022-01-24 09:35:13.231653', 26, 'EXECUTED', '7:b82ffb34850fa0836be16deefc6a87c4', 'createIndex indexName=IDX_USER_EMAIL, tableName=USER_ENTITY; createIndex indexName=IDX_USER_ROLE_MAPPING, tableName=USER_ROLE_MAPPING; createIndex indexName=IDX_USER_GROUP_MAPPING, tableName=USER_GROUP_MEMBERSHIP; createIndex indexName=IDX_USER_CO...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-2.0.0', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-2.0.0.xml', '2022-01-24 09:35:13.357627', 27, 'EXECUTED', '7:9cc98082921330d8d9266decdd4bd658', 'createTable tableName=RESOURCE_SERVER; addPrimaryKey constraintName=CONSTRAINT_FARS, tableName=RESOURCE_SERVER; addUniqueConstraint constraintName=UK_AU8TT6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER; createTable tableName=RESOURCE_SERVER_RESOU...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-2.5.1', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-2.5.1.xml', '2022-01-24 09:35:13.364849', 28, 'EXECUTED', '7:03d64aeed9cb52b969bd30a7ac0db57e', 'update tableName=RESOURCE_SERVER_POLICY', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.1.0-KEYCLOAK-5461', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.1.0.xml', '2022-01-24 09:35:13.470989', 29, 'EXECUTED', '7:f1f9fd8710399d725b780f463c6b21cd', 'createTable tableName=BROKER_LINK; createTable tableName=FED_USER_ATTRIBUTE; createTable tableName=FED_USER_CONSENT; createTable tableName=FED_USER_CONSENT_ROLE; createTable tableName=FED_USER_CONSENT_PROT_MAPPER; createTable tableName=FED_USER_CR...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.2.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.2.0.xml', '2022-01-24 09:35:13.49963', 30, 'EXECUTED', '7:53188c3eb1107546e6f765835705b6c1', 'addColumn tableName=ADMIN_EVENT_ENTITY; createTable tableName=CREDENTIAL_ATTRIBUTE; createTable tableName=FED_CREDENTIAL_ATTRIBUTE; modifyDataType columnName=VALUE, tableName=CREDENTIAL; addForeignKeyConstraint baseTableName=FED_CREDENTIAL_ATTRIBU...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.3.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.3.0.xml', '2022-01-24 09:35:13.522638', 31, 'EXECUTED', '7:d6e6f3bc57a0c5586737d1351725d4d4', 'createTable tableName=FEDERATED_USER; addPrimaryKey constraintName=CONSTR_FEDERATED_USER, tableName=FEDERATED_USER; dropDefaultValue columnName=TOTP, tableName=USER_ENTITY; dropColumn columnName=TOTP, tableName=USER_ENTITY; addColumn tableName=IDE...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.4.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.4.0.xml', '2022-01-24 09:35:13.527885', 32, 'EXECUTED', '7:454d604fbd755d9df3fd9c6329043aa5', 'customChange', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.5.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2022-01-24 09:35:13.534732', 33, 'EXECUTED', '7:57e98a3077e29caf562f7dbf80c72600', 'customChange; modifyDataType columnName=USER_ID, tableName=OFFLINE_USER_SESSION', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.5.0-unicode-oracle', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2022-01-24 09:35:13.537887', 34, 'MARK_RAN', '7:e4c7e8f2256210aee71ddc42f538b57a', 'modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.5.0-unicode-other-dbs', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2022-01-24 09:35:13.583167', 35, 'EXECUTED', '7:09a43c97e49bc626460480aa1379b522', 'modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.5.0-duplicate-email-support', 'slawomir@dabek.name', 'META-INF/jpa-changelog-2.5.0.xml', '2022-01-24 09:35:13.590416', 36, 'EXECUTED', '7:26bfc7c74fefa9126f2ce702fb775553', 'addColumn tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.5.0-unique-group-names', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2022-01-24 09:35:13.600938', 37, 'EXECUTED', '7:a161e2ae671a9020fff61e996a207377', 'addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('2.5.1', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.5.1.xml', '2022-01-24 09:35:13.606566', 38, 'EXECUTED', '7:37fc1781855ac5388c494f1442b3f717', 'addColumn tableName=FED_USER_CONSENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.0.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-3.0.0.xml', '2022-01-24 09:35:13.612423', 39, 'EXECUTED', '7:13a27db0dae6049541136adad7261d27', 'addColumn tableName=IDENTITY_PROVIDER', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.2.0-fix', 'keycloak', 'META-INF/jpa-changelog-3.2.0.xml', '2022-01-24 09:35:13.615295', 40, 'MARK_RAN', '7:550300617e3b59e8af3a6294df8248a3', 'addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.2.0-fix-with-keycloak-5416', 'keycloak', 'META-INF/jpa-changelog-3.2.0.xml', '2022-01-24 09:35:13.618423', 41, 'MARK_RAN', '7:e3a9482b8931481dc2772a5c07c44f17', 'dropIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS; addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS; createIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.2.0-fix-offline-sessions', 'hmlnarik', 'META-INF/jpa-changelog-3.2.0.xml', '2022-01-24 09:35:13.624161', 42, 'EXECUTED', '7:72b07d85a2677cb257edb02b408f332d', 'customChange', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.2.0-fixed', 'keycloak', 'META-INF/jpa-changelog-3.2.0.xml', '2022-01-24 09:35:14.407295', 43, 'EXECUTED', '7:a72a7858967bd414835d19e04d880312', 'addColumn tableName=REALM; dropPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_PK2, tableName=OFFLINE_CLIENT_SESSION; dropColumn columnName=CLIENT_SESSION_ID, tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_P...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.3.0', 'keycloak', 'META-INF/jpa-changelog-3.3.0.xml', '2022-01-24 09:35:14.413437', 44, 'EXECUTED', '7:94edff7cf9ce179e7e85f0cd78a3cf2c', 'addColumn tableName=USER_ENTITY', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part2-KEYCLOAK-6095', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2022-01-24 09:35:14.423829', 46, 'EXECUTED', '7:e64b5dcea7db06077c6e57d3b9e5ca14', 'customChange', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2022-01-24 09:35:14.426773', 47, 'MARK_RAN', '7:fd8cf02498f8b1e72496a20afc75178c', 'dropIndex indexName=IDX_RES_SERV_POL_RES_SERV, tableName=RESOURCE_SERVER_POLICY; dropIndex indexName=IDX_RES_SRV_RES_RES_SRV, tableName=RESOURCE_SERVER_RESOURCE; dropIndex indexName=IDX_RES_SRV_SCOPE_RES_SRV, tableName=RESOURCE_SERVER_SCOPE', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed-nodropindex', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2022-01-24 09:35:14.531332', 48, 'EXECUTED', '7:542794f25aa2b1fbabb7e577d6646319', 'addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_POLICY; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_RESOURCE; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, ...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authn-3.4.0.CR1-refresh-token-max-reuse', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2022-01-24 09:35:14.536557', 49, 'EXECUTED', '7:edad604c882df12f74941dac3cc6d650', 'addColumn tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.4.0', 'keycloak', 'META-INF/jpa-changelog-3.4.0.xml', '2022-01-24 09:35:14.610754', 50, 'EXECUTED', '7:0f88b78b7b46480eb92690cbf5e44900', 'addPrimaryKey constraintName=CONSTRAINT_REALM_DEFAULT_ROLES, tableName=REALM_DEFAULT_ROLES; addPrimaryKey constraintName=CONSTRAINT_COMPOSITE_ROLE, tableName=COMPOSITE_ROLE; addPrimaryKey constraintName=CONSTR_REALM_DEFAULT_GROUPS, tableName=REALM...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.4.0-KEYCLOAK-5230', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-3.4.0.xml', '2022-01-24 09:35:14.790271', 51, 'EXECUTED', '7:d560e43982611d936457c327f872dd59', 'createIndex indexName=IDX_FU_ATTRIBUTE, tableName=FED_USER_ATTRIBUTE; createIndex indexName=IDX_FU_CONSENT, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CONSENT_RU, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CREDENTIAL, t...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.4.1', 'psilva@redhat.com', 'META-INF/jpa-changelog-3.4.1.xml', '2022-01-24 09:35:14.795521', 52, 'EXECUTED', '7:c155566c42b4d14ef07059ec3b3bbd8e', 'modifyDataType columnName=VALUE, tableName=CLIENT_ATTRIBUTES', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.4.2', 'keycloak', 'META-INF/jpa-changelog-3.4.2.xml', '2022-01-24 09:35:14.799948', 53, 'EXECUTED', '7:b40376581f12d70f3c89ba8ddf5b7dea', 'update tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('3.4.2-KEYCLOAK-5172', 'mkanis@redhat.com', 'META-INF/jpa-changelog-3.4.2.xml', '2022-01-24 09:35:14.803198', 54, 'EXECUTED', '7:a1132cc395f7b95b3646146c2e38f168', 'update tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.0.0-KEYCLOAK-6335', 'bburke@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2022-01-24 09:35:14.812377', 55, 'EXECUTED', '7:d8dc5d89c789105cfa7ca0e82cba60af', 'createTable tableName=CLIENT_AUTH_FLOW_BINDINGS; addPrimaryKey constraintName=C_CLI_FLOW_BIND, tableName=CLIENT_AUTH_FLOW_BINDINGS', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.0.0-CLEANUP-UNUSED-TABLE', 'bburke@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2022-01-24 09:35:14.8207', 56, 'EXECUTED', '7:7822e0165097182e8f653c35517656a3', 'dropTable tableName=CLIENT_IDENTITY_PROV_MAPPING', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.0.0-KEYCLOAK-6228', 'bburke@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2022-01-24 09:35:14.864154', 57, 'EXECUTED', '7:c6538c29b9c9a08f9e9ea2de5c2b6375', 'dropUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHOGM8UEWRT, tableName=USER_CONSENT; dropNotNullConstraint columnName=CLIENT_ID, tableName=USER_CONSENT; addColumn tableName=USER_CONSENT; addUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHO...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.0.0-KEYCLOAK-5579-fixed', 'mposolda@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2022-01-24 09:35:15.135325', 58, 'EXECUTED', '7:6d4893e36de22369cf73bcb051ded875', 'dropForeignKeyConstraint baseTableName=CLIENT_TEMPLATE_ATTRIBUTES, constraintName=FK_CL_TEMPL_ATTR_TEMPL; renameTable newTableName=CLIENT_SCOPE_ATTRIBUTES, oldTableName=CLIENT_TEMPLATE_ATTRIBUTES; renameColumn newColumnName=SCOPE_ID, oldColumnName...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-4.0.0.CR1', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-4.0.0.CR1.xml', '2022-01-24 09:35:15.182867', 59, 'EXECUTED', '7:57960fc0b0f0dd0563ea6f8b2e4a1707', 'createTable tableName=RESOURCE_SERVER_PERM_TICKET; addPrimaryKey constraintName=CONSTRAINT_FAPMT, tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRHO213XCX4WNKOG82SSPMT...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-4.0.0.Beta3', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-4.0.0.Beta3.xml', '2022-01-24 09:35:15.190287', 60, 'EXECUTED', '7:2b4b8bff39944c7097977cc18dbceb3b', 'addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRPO2128CX4WNKOG82SSRFY, referencedTableName=RESOURCE_SERVER_POLICY', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-4.2.0.Final', 'mhajas@redhat.com', 'META-INF/jpa-changelog-authz-4.2.0.Final.xml', '2022-01-24 09:35:15.19952', 61, 'EXECUTED', '7:2aa42a964c59cd5b8ca9822340ba33a8', 'createTable tableName=RESOURCE_URIS; addForeignKeyConstraint baseTableName=RESOURCE_URIS, constraintName=FK_RESOURCE_SERVER_URIS, referencedTableName=RESOURCE_SERVER_RESOURCE; customChange; dropColumn columnName=URI, tableName=RESOURCE_SERVER_RESO...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-4.2.0.Final-KEYCLOAK-9944', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-authz-4.2.0.Final.xml', '2022-01-24 09:35:15.207187', 62, 'EXECUTED', '7:9ac9e58545479929ba23f4a3087a0346', 'addPrimaryKey constraintName=CONSTRAINT_RESOUR_URIS_PK, tableName=RESOURCE_URIS', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.2.0-KEYCLOAK-6313', 'wadahiro@gmail.com', 'META-INF/jpa-changelog-4.2.0.xml', '2022-01-24 09:35:15.212566', 63, 'EXECUTED', '7:14d407c35bc4fe1976867756bcea0c36', 'addColumn tableName=REQUIRED_ACTION_PROVIDER', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.3.0-KEYCLOAK-7984', 'wadahiro@gmail.com', 'META-INF/jpa-changelog-4.3.0.xml', '2022-01-24 09:35:15.215702', 64, 'EXECUTED', '7:241a8030c748c8548e346adee548fa93', 'update tableName=REQUIRED_ACTION_PROVIDER', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.6.0-KEYCLOAK-7950', 'psilva@redhat.com', 'META-INF/jpa-changelog-4.6.0.xml', '2022-01-24 09:35:15.218722', 65, 'EXECUTED', '7:7d3182f65a34fcc61e8d23def037dc3f', 'update tableName=RESOURCE_SERVER_RESOURCE', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.6.0-KEYCLOAK-8377', 'keycloak', 'META-INF/jpa-changelog-4.6.0.xml', '2022-01-24 09:35:15.250844', 66, 'EXECUTED', '7:b30039e00a0b9715d430d1b0636728fa', 'createTable tableName=ROLE_ATTRIBUTE; addPrimaryKey constraintName=CONSTRAINT_ROLE_ATTRIBUTE_PK, tableName=ROLE_ATTRIBUTE; addForeignKeyConstraint baseTableName=ROLE_ATTRIBUTE, constraintName=FK_ROLE_ATTRIBUTE_ID, referencedTableName=KEYCLOAK_ROLE...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.6.0-KEYCLOAK-8555', 'gideonray@gmail.com', 'META-INF/jpa-changelog-4.6.0.xml', '2022-01-24 09:35:15.26761', 67, 'EXECUTED', '7:3797315ca61d531780f8e6f82f258159', 'createIndex indexName=IDX_COMPONENT_PROVIDER_TYPE, tableName=COMPONENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.7.0-KEYCLOAK-1267', 'sguilhen@redhat.com', 'META-INF/jpa-changelog-4.7.0.xml', '2022-01-24 09:35:15.273473', 68, 'EXECUTED', '7:c7aa4c8d9573500c2d347c1941ff0301', 'addColumn tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.7.0-KEYCLOAK-7275', 'keycloak', 'META-INF/jpa-changelog-4.7.0.xml', '2022-01-24 09:35:15.301921', 69, 'EXECUTED', '7:b207faee394fc074a442ecd42185a5dd', 'renameColumn newColumnName=CREATED_ON, oldColumnName=LAST_SESSION_REFRESH, tableName=OFFLINE_USER_SESSION; addNotNullConstraint columnName=CREATED_ON, tableName=OFFLINE_USER_SESSION; addColumn tableName=OFFLINE_USER_SESSION; customChange; createIn...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('4.8.0-KEYCLOAK-8835', 'sguilhen@redhat.com', 'META-INF/jpa-changelog-4.8.0.xml', '2022-01-24 09:35:15.308525', 70, 'EXECUTED', '7:ab9a9762faaba4ddfa35514b212c4922', 'addNotNullConstraint columnName=SSO_MAX_LIFESPAN_REMEMBER_ME, tableName=REALM; addNotNullConstraint columnName=SSO_IDLE_TIMEOUT_REMEMBER_ME, tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('authz-7.0.0-KEYCLOAK-10443', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-7.0.0.xml', '2022-01-24 09:35:15.314334', 71, 'EXECUTED', '7:b9710f74515a6ccb51b72dc0d19df8c4', 'addColumn tableName=RESOURCE_SERVER', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('8.0.0-adding-credential-columns', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2022-01-24 09:35:15.321955', 72, 'EXECUTED', '7:ec9707ae4d4f0b7452fee20128083879', 'addColumn tableName=CREDENTIAL; addColumn tableName=FED_USER_CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('8.0.0-updating-credential-data-not-oracle-fixed', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2022-01-24 09:35:15.32843', 73, 'EXECUTED', '7:3979a0ae07ac465e920ca696532fc736', 'update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('8.0.0-updating-credential-data-oracle-fixed', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2022-01-24 09:35:15.331306', 74, 'MARK_RAN', '7:5abfde4c259119d143bd2fbf49ac2bca', 'update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('8.0.0-credential-cleanup-fixed', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2022-01-24 09:35:15.354969', 75, 'EXECUTED', '7:b48da8c11a3d83ddd6b7d0c8c2219345', 'dropDefaultValue columnName=COUNTER, tableName=CREDENTIAL; dropDefaultValue columnName=DIGITS, tableName=CREDENTIAL; dropDefaultValue columnName=PERIOD, tableName=CREDENTIAL; dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; dropColumn ...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('8.0.0-resource-tag-support', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2022-01-24 09:35:15.378897', 76, 'EXECUTED', '7:a73379915c23bfad3e8f5c6d5c0aa4bd', 'addColumn tableName=MIGRATION_MODEL; createIndex indexName=IDX_UPDATE_TIME, tableName=MIGRATION_MODEL', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.0-always-display-client', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2022-01-24 09:35:15.394707', 77, 'EXECUTED', '7:39e0073779aba192646291aa2332493d', 'addColumn tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.0-drop-constraints-for-column-increase', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2022-01-24 09:35:15.397585', 78, 'MARK_RAN', '7:81f87368f00450799b4bf42ea0b3ec34', 'dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5PMT, tableName=RESOURCE_SERVER_PERM_TICKET; dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER_RESOURCE; dropPrimaryKey constraintName=CONSTRAINT_O...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.0-increase-column-size-federated-fk', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2022-01-24 09:35:15.437653', 79, 'EXECUTED', '7:20b37422abb9fb6571c618148f013a15', 'modifyDataType columnName=CLIENT_ID, tableName=FED_USER_CONSENT; modifyDataType columnName=CLIENT_REALM_CONSTRAINT, tableName=KEYCLOAK_ROLE; modifyDataType columnName=OWNER, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=CLIENT_ID, ta...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.0-recreate-constraints-after-column-increase', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2022-01-24 09:35:15.440391', 80, 'MARK_RAN', '7:1970bb6cfb5ee800736b95ad3fb3c78a', 'addNotNullConstraint columnName=CLIENT_ID, tableName=OFFLINE_CLIENT_SESSION; addNotNullConstraint columnName=OWNER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNullConstraint columnName=REQUESTER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNull...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.1-add-index-to-client.client_id', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2022-01-24 09:35:15.45953', 81, 'EXECUTED', '7:45d9b25fc3b455d522d8dcc10a0f4c80', 'createIndex indexName=IDX_CLIENT_ID, tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.1-KEYCLOAK-12579-drop-constraints', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2022-01-24 09:35:15.46277', 82, 'MARK_RAN', '7:890ae73712bc187a66c2813a724d037f', 'dropUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.1-KEYCLOAK-12579-add-not-null-constraint', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2022-01-24 09:35:15.468934', 83, 'EXECUTED', '7:0a211980d27fafe3ff50d19a3a29b538', 'addNotNullConstraint columnName=PARENT_GROUP, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.1-KEYCLOAK-12579-recreate-constraints', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2022-01-24 09:35:15.471883', 84, 'MARK_RAN', '7:a161e2ae671a9020fff61e996a207377', 'addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('9.0.1-add-index-to-events', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2022-01-24 09:35:15.491551', 85, 'EXECUTED', '7:01c49302201bdf815b0a18d1f98a55dc', 'createIndex indexName=IDX_EVENT_TIME, tableName=EVENT_ENTITY', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('map-remove-ri', 'keycloak', 'META-INF/jpa-changelog-11.0.0.xml', '2022-01-24 09:35:15.500372', 86, 'EXECUTED', '7:3dace6b144c11f53f1ad2c0361279b86', 'dropForeignKeyConstraint baseTableName=REALM, constraintName=FK_TRAF444KK6QRKMS7N56AIWQ5Y; dropForeignKeyConstraint baseTableName=KEYCLOAK_ROLE, constraintName=FK_KJHO5LE2C0RAL09FL8CM9WFW9', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('map-remove-ri', 'keycloak', 'META-INF/jpa-changelog-12.0.0.xml', '2022-01-24 09:35:15.515473', 87, 'EXECUTED', '7:578d0b92077eaf2ab95ad0ec087aa903', 'dropForeignKeyConstraint baseTableName=REALM_DEFAULT_GROUPS, constraintName=FK_DEF_GROUPS_GROUP; dropForeignKeyConstraint baseTableName=REALM_DEFAULT_ROLES, constraintName=FK_H4WPD7W4HSOOLNI3H0SW7BTJE; dropForeignKeyConstraint baseTableName=CLIENT...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('12.1.0-add-realm-localization-table', 'keycloak', 'META-INF/jpa-changelog-12.0.0.xml', '2022-01-24 09:35:15.531847', 88, 'EXECUTED', '7:c95abe90d962c57a09ecaee57972835d', 'createTable tableName=REALM_LOCALIZATIONS; addPrimaryKey tableName=REALM_LOCALIZATIONS', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('default-roles', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.53962', 89, 'EXECUTED', '7:f1313bcc2994a5c4dc1062ed6d8282d3', 'addColumn tableName=REALM; customChange', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('default-roles-cleanup', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.55155', 90, 'EXECUTED', '7:90d763b52eaffebefbcbde55f269508b', 'dropTable tableName=REALM_DEFAULT_ROLES; dropTable tableName=CLIENT_DEFAULT_ROLES', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('13.0.0-KEYCLOAK-16844', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.571364', 91, 'EXECUTED', '7:d554f0cb92b764470dccfa5e0014a7dd', 'createIndex indexName=IDX_OFFLINE_USS_PRELOAD, tableName=OFFLINE_USER_SESSION', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('map-remove-ri-13.0.0', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.584562', 92, 'EXECUTED', '7:73193e3ab3c35cf0f37ccea3bf783764', 'dropForeignKeyConstraint baseTableName=DEFAULT_CLIENT_SCOPE, constraintName=FK_R_DEF_CLI_SCOPE_SCOPE; dropForeignKeyConstraint baseTableName=CLIENT_SCOPE_CLIENT, constraintName=FK_C_CLI_SCOPE_SCOPE; dropForeignKeyConstraint baseTableName=CLIENT_SC...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('13.0.0-KEYCLOAK-17992-drop-constraints', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.587114', 93, 'MARK_RAN', '7:90a1e74f92e9cbaa0c5eab80b8a037f3', 'dropPrimaryKey constraintName=C_CLI_SCOPE_BIND, tableName=CLIENT_SCOPE_CLIENT; dropIndex indexName=IDX_CLSCOPE_CL, tableName=CLIENT_SCOPE_CLIENT; dropIndex indexName=IDX_CL_CLSCOPE, tableName=CLIENT_SCOPE_CLIENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('13.0.0-increase-column-size-federated', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.600354', 94, 'EXECUTED', '7:5b9248f29cd047c200083cc6d8388b16', 'modifyDataType columnName=CLIENT_ID, tableName=CLIENT_SCOPE_CLIENT; modifyDataType columnName=SCOPE_ID, tableName=CLIENT_SCOPE_CLIENT', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('13.0.0-KEYCLOAK-17992-recreate-constraints', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.60303', 95, 'MARK_RAN', '7:64db59e44c374f13955489e8990d17a1', 'addNotNullConstraint columnName=CLIENT_ID, tableName=CLIENT_SCOPE_CLIENT; addNotNullConstraint columnName=SCOPE_ID, tableName=CLIENT_SCOPE_CLIENT; addPrimaryKey constraintName=C_CLI_SCOPE_BIND, tableName=CLIENT_SCOPE_CLIENT; createIndex indexName=...', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('json-string-accomodation-fixed', 'keycloak', 'META-INF/jpa-changelog-13.0.0.xml', '2022-01-24 09:35:15.610038', 96, 'EXECUTED', '7:329a578cdb43262fff975f0a7f6cda60', 'addColumn tableName=REALM_ATTRIBUTE; update tableName=REALM_ATTRIBUTE; dropColumn columnName=VALUE, tableName=REALM_ATTRIBUTE; renameColumn newColumnName=VALUE, oldColumnName=VALUE_NEW, tableName=REALM_ATTRIBUTE', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('14.0.0-KEYCLOAK-11019', 'keycloak', 'META-INF/jpa-changelog-14.0.0.xml', '2022-01-24 09:35:15.659286', 97, 'EXECUTED', '7:fae0de241ac0fd0bbc2b380b85e4f567', 'createIndex indexName=IDX_OFFLINE_CSS_PRELOAD, tableName=OFFLINE_CLIENT_SESSION; createIndex indexName=IDX_OFFLINE_USS_BY_USER, tableName=OFFLINE_USER_SESSION; createIndex indexName=IDX_OFFLINE_USS_BY_USERSESS, tableName=OFFLINE_USER_SESSION', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('14.0.0-KEYCLOAK-18286', 'keycloak', 'META-INF/jpa-changelog-14.0.0.xml', '2022-01-24 09:35:15.662118', 98, 'MARK_RAN', '7:075d54e9180f49bb0c64ca4218936e81', 'createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('14.0.0-KEYCLOAK-18286-revert', 'keycloak', 'META-INF/jpa-changelog-14.0.0.xml', '2022-01-24 09:35:15.673618', 99, 'MARK_RAN', '7:06499836520f4f6b3d05e35a59324910', 'dropIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('14.0.0-KEYCLOAK-18286-supported-dbs', 'keycloak', 'META-INF/jpa-changelog-14.0.0.xml', '2022-01-24 09:35:15.69557', 100, 'EXECUTED', '7:fad08e83c77d0171ec166bc9bc5d390a', 'createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('14.0.0-KEYCLOAK-18286-unsupported-dbs', 'keycloak', 'META-INF/jpa-changelog-14.0.0.xml', '2022-01-24 09:35:15.698343', 101, 'MARK_RAN', '7:3d2b23076e59c6f70bae703aa01be35b', 'createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('KEYCLOAK-17267-add-index-to-user-attributes', 'keycloak', 'META-INF/jpa-changelog-14.0.0.xml', '2022-01-24 09:35:15.717243', 102, 'EXECUTED', '7:1a7f28ff8d9e53aeb879d76ea3d9341a', 'createIndex indexName=IDX_USER_ATTRIBUTE_NAME, tableName=USER_ATTRIBUTE', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('KEYCLOAK-18146-add-saml-art-binding-identifier', 'keycloak', 'META-INF/jpa-changelog-14.0.0.xml', '2022-01-24 09:35:15.721926', 103, 'EXECUTED', '7:2fd554456fed4a82c698c555c5b751b6', 'customChange', '', NULL, '3.5.4', NULL, NULL, '3016911733');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('15.0.0-KEYCLOAK-18467', 'keycloak', 'META-INF/jpa-changelog-15.0.0.xml', '2022-01-24 09:35:15.728915', 104, 'EXECUTED', '7:b06356d66c2790ecc2ae54ba0458397a', 'addColumn tableName=REALM_LOCALIZATIONS; update tableName=REALM_LOCALIZATIONS; dropColumn columnName=TEXTS, tableName=REALM_LOCALIZATIONS; renameColumn newColumnName=TEXTS, oldColumnName=TEXTS_NEW, tableName=REALM_LOCALIZATIONS; addNotNullConstrai...', '', NULL, '3.5.4', NULL, NULL, '3016911733');


--
-- TOC entry 3841 (class 0 OID 16387)
-- Dependencies: 200
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.databasechangeloglock (id, locked, lockgranted, lockedby) VALUES (1, false, NULL, NULL);
INSERT INTO public.databasechangeloglock (id, locked, lockgranted, lockedby) VALUES (1000, false, NULL, NULL);
INSERT INTO public.databasechangeloglock (id, locked, lockgranted, lockedby) VALUES (1001, false, NULL, NULL);


--
-- TOC entry 3925 (class 0 OID 17841)
-- Dependencies: 284
-- Data for Name: default_client_scope; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', 'e0d97369-c5cc-4444-b0ac-9138bd2a0bdd', false);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', 'c18c5ada-6579-4a8c-ab75-0f23f563a2b6', true);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0', true);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', '0cef8854-46a9-4dc4-98c1-35b9d10d5a59', true);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', 'd3905410-7155-4fad-a2a5-5155f64dee8a', false);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', '45b07521-a8a3-466c-a36d-ada9717ed9b4', false);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', '38de0fa7-5b2b-4cb9-921c-0f16a124eed1', true);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', 'd2708b0a-4401-4da9-abea-9ca33e2cd8db', true);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('master', 'f3ed45d6-a944-4af5-aeb4-79291fabc9be', false);
INSERT INTO public.default_client_scope (realm_id, scope_id, default_scope) VALUES ('university', '418e9be3-8b8a-4542-8b9f-f965312bb7cc', true);


--
-- TOC entry 3848 (class 0 OID 16431)
-- Dependencies: 207
-- Data for Name: event_entity; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3913 (class 0 OID 17530)
-- Dependencies: 272
-- Data for Name: fed_user_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3914 (class 0 OID 17536)
-- Dependencies: 273
-- Data for Name: fed_user_consent; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3927 (class 0 OID 17867)
-- Dependencies: 286
-- Data for Name: fed_user_consent_cl_scope; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3915 (class 0 OID 17545)
-- Dependencies: 274
-- Data for Name: fed_user_credential; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3916 (class 0 OID 17555)
-- Dependencies: 275
-- Data for Name: fed_user_group_membership; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3917 (class 0 OID 17558)
-- Dependencies: 276
-- Data for Name: fed_user_required_action; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3918 (class 0 OID 17565)
-- Dependencies: 277
-- Data for Name: fed_user_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3871 (class 0 OID 16825)
-- Dependencies: 230
-- Data for Name: federated_identity; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3921 (class 0 OID 17634)
-- Dependencies: 280
-- Data for Name: federated_user; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3897 (class 0 OID 17244)
-- Dependencies: 256
-- Data for Name: group_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3896 (class 0 OID 17241)
-- Dependencies: 255
-- Data for Name: group_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3872 (class 0 OID 16831)
-- Dependencies: 231
-- Data for Name: identity_provider; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3873 (class 0 OID 16841)
-- Dependencies: 232
-- Data for Name: identity_provider_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3878 (class 0 OID 16947)
-- Dependencies: 237
-- Data for Name: identity_provider_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3879 (class 0 OID 16953)
-- Dependencies: 238
-- Data for Name: idp_mapper_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3895 (class 0 OID 17238)
-- Dependencies: 254
-- Data for Name: keycloak_group; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.keycloak_group (id, name, parent_group, realm_id) VALUES ('55703d9c-07dc-40b0-abee-12df5cb35bdb', 'users', ' ', 'master');
INSERT INTO public.keycloak_group (id, name, parent_group, realm_id) VALUES ('bf3d9d3c-e890-4cb9-9502-841bf1ed07b0', 'admins', ' ', 'master');


--
-- TOC entry 3849 (class 0 OID 16440)
-- Dependencies: 208
-- Data for Name: keycloak_role; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('d58f3db5-7572-41c6-b871-530e48d3ab60', 'master', false, '${role_default-roles}', 'default-roles-master', 'master', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', 'master', false, '${role_admin}', 'admin', 'master', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('658f6e15-2f2d-4603-b934-a824230f46f3', 'master', false, '${role_create-realm}', 'create-realm', 'master', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('2eaf7071-2108-4257-b084-7c8b92227c43', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_create-client}', 'create-client', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('086b6804-1b83-4e99-a92b-ef075dac19a8', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_view-realm}', 'view-realm', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('044d2ee4-5102-4ea4-8f41-c7d5417fa34b', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_view-users}', 'view-users', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('fcf42468-fe3f-4c17-bdb0-4eaa7469e166', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_view-clients}', 'view-clients', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('971ed0c8-7ad9-4d17-915d-2cac20559ce9', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_view-events}', 'view-events', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('6b18111e-9eec-481a-a857-a5fb2a46d920', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_view-identity-providers}', 'view-identity-providers', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('3ac1a25b-d51e-4d07-a005-80de4fbc6e53', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_view-authorization}', 'view-authorization', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('9798391f-d21f-4b1a-a580-a64991787dae', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_manage-realm}', 'manage-realm', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('22fafafa-df01-4195-8610-18aae4526919', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_manage-users}', 'manage-users', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('07331812-fd59-43b3-9e80-d6ac5095e9c8', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_manage-clients}', 'manage-clients', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('cf8e1d41-c2b1-4f7b-8417-0bb43f784b6e', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_manage-events}', 'manage-events', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('e277078d-1182-4e3e-90f3-d1cd6a4d4cae', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_manage-identity-providers}', 'manage-identity-providers', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('cdd3818a-9882-4a1f-83cc-a7c243471d03', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_manage-authorization}', 'manage-authorization', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('10709f9b-6487-495e-91da-ef00b7702d9f', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_query-users}', 'query-users', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('1aa9da8b-1bbd-44d8-9333-1a0d4c2fc262', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_query-clients}', 'query-clients', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('9ee4b552-dc41-44ff-9002-c78a781dc4f4', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_query-realms}', 'query-realms', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('dbb1fd86-cbba-4897-897b-7ee66ffab456', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_query-groups}', 'query-groups', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('f00e5ad7-ea36-4433-99f6-e52856a9e824', 'ab880e32-c897-43c4-bea8-78637e23b4ec', true, '${role_view-profile}', 'view-profile', 'master', 'ab880e32-c897-43c4-bea8-78637e23b4ec', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('62bd7fab-4794-4f6c-a5de-946763965c3b', 'ab880e32-c897-43c4-bea8-78637e23b4ec', true, '${role_manage-account}', 'manage-account', 'master', 'ab880e32-c897-43c4-bea8-78637e23b4ec', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('2c8c5e12-5522-487a-980f-d45cf668a2ea', 'ab880e32-c897-43c4-bea8-78637e23b4ec', true, '${role_manage-account-links}', 'manage-account-links', 'master', 'ab880e32-c897-43c4-bea8-78637e23b4ec', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('fd82ad56-5404-4760-935c-25498f17872b', 'ab880e32-c897-43c4-bea8-78637e23b4ec', true, '${role_view-applications}', 'view-applications', 'master', 'ab880e32-c897-43c4-bea8-78637e23b4ec', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('3813c239-f36a-461d-9b39-4154261993b3', 'ab880e32-c897-43c4-bea8-78637e23b4ec', true, '${role_view-consent}', 'view-consent', 'master', 'ab880e32-c897-43c4-bea8-78637e23b4ec', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('190f5e7f-954f-4f72-9519-96044c4f64d6', 'ab880e32-c897-43c4-bea8-78637e23b4ec', true, '${role_manage-consent}', 'manage-consent', 'master', 'ab880e32-c897-43c4-bea8-78637e23b4ec', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('6fd3366b-7f28-43f9-91e5-0a41f9e82259', 'ab880e32-c897-43c4-bea8-78637e23b4ec', true, '${role_delete-account}', 'delete-account', 'master', 'ab880e32-c897-43c4-bea8-78637e23b4ec', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('cbc5a0a5-de50-4b3b-9f73-f007008c2471', '06c77aa1-f92e-4376-82ba-0f73269e3f95', true, '${role_read-token}', 'read-token', 'master', '06c77aa1-f92e-4376-82ba-0f73269e3f95', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('247b338a-9a45-45ff-b962-2c0c9daa5908', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', true, '${role_impersonation}', 'impersonation', 'master', '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('a2b82d25-8314-4e4f-bf55-7a152eb6ce62', 'master', false, '${role_offline-access}', 'offline_access', 'master', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('58232c19-3ca8-49bf-9262-19547bf72864', 'master', false, '${role_uma_authorization}', 'uma_authorization', 'master', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('0d4b0f93-41a0-4794-871e-8fa2cd402992', 'university', false, '${role_default-roles}', 'default-roles-university', 'university', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('fce01f44-4fc5-40d8-a341-2c33d6a0b9e6', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_create-client}', 'create-client', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('bc8ccc9b-c507-4a28-a0ba-b06648f2c155', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_view-realm}', 'view-realm', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('f8cc984e-0088-472e-83f6-fca0a5e6392b', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_view-users}', 'view-users', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('2c10980d-a45a-465f-a5c8-79185070ec04', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_view-clients}', 'view-clients', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('a5895c2c-6c9c-40ab-aff7-c7d114438d6c', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_view-events}', 'view-events', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('cabdaa89-998f-4c56-9c98-b62491e582f8', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_view-identity-providers}', 'view-identity-providers', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('bc8668cc-e3cb-47f1-be2f-dfc417bb3b75', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_view-authorization}', 'view-authorization', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('67859da9-37d7-4673-928d-611c8177e71a', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_manage-realm}', 'manage-realm', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('bb3435ce-e19d-45fc-8773-a91c8b2bd635', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_manage-users}', 'manage-users', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('56e049c6-cf93-47bc-af49-877f0c1c9945', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_manage-clients}', 'manage-clients', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('cf871e78-2716-4c81-9ea5-da33841779a3', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_manage-events}', 'manage-events', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('c29cc501-6b1b-4252-bdc1-60d9a7a1c325', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_manage-identity-providers}', 'manage-identity-providers', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('ec77e898-bb39-4e15-89f4-23f43287af8e', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_manage-authorization}', 'manage-authorization', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('bb0472bb-594e-4583-8b88-2f6970109bae', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_query-users}', 'query-users', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('e4188e1a-d947-431c-81a4-a2b876f0e4ce', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_query-clients}', 'query-clients', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('3042ec43-5d7f-4e51-b353-08f6db15d78f', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_query-realms}', 'query-realms', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('85980e95-1b7d-4751-9b09-fe95f64f2bdd', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_query-groups}', 'query-groups', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('d3ed3737-dfd9-4ba7-96e0-abef8938c269', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_realm-admin}', 'realm-admin', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('5ad18a5f-9f5e-4f19-91e6-443e45c70420', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_create-client}', 'create-client', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('269fbdce-2d82-4e92-aec1-5fbcb274f58a', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_view-realm}', 'view-realm', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('b1f73988-1403-454a-a88e-97316009ad66', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_view-users}', 'view-users', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('8c5454ea-cacd-4410-950e-f4dd2d991bb6', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_view-clients}', 'view-clients', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('2581c79c-ac67-409e-8ff2-5437e1f32958', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_view-events}', 'view-events', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('34a46acd-9356-4386-b0bf-c6bb7d50ea5c', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_view-identity-providers}', 'view-identity-providers', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('76b2d949-dd8a-4850-bf9c-2d5057d8bba7', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_view-authorization}', 'view-authorization', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('a3e4be50-f930-4a91-afbb-0a2768a8c14b', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_manage-realm}', 'manage-realm', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('c1e13e53-09a1-4368-b0e0-8a61c4d9b9b5', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_manage-users}', 'manage-users', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('aa58067d-617a-411c-a48d-0b7e86bbf6e5', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_manage-clients}', 'manage-clients', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('b7d2bbcc-8bfb-4dd9-97b5-708e9646a3c5', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_manage-events}', 'manage-events', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('a372e2b7-19bd-4883-b33f-994b5c914bb4', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_manage-identity-providers}', 'manage-identity-providers', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('1d1c6f09-ae74-4b37-8109-ffcbad9527ab', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_manage-authorization}', 'manage-authorization', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('f5d7b9d9-4590-4ee9-b317-9021908eec69', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_query-users}', 'query-users', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('5078ce17-9aef-45b6-adf8-61d7d40248d6', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_query-clients}', 'query-clients', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('7aa83dcd-e332-409d-81c0-42ebd529338a', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_query-realms}', 'query-realms', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('57835ab4-1993-4bdc-ab6a-83235dd5940b', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_query-groups}', 'query-groups', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('238eb159-1108-4290-9540-ce186a0e75a5', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, '${role_view-profile}', 'view-profile', 'university', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('197755ea-6368-4aca-a626-813a9d720e11', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, '${role_manage-account}', 'manage-account', 'university', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('dcf926a0-ad05-4b11-bb40-7f6e08be28ed', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, '${role_manage-account-links}', 'manage-account-links', 'university', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('0661e6ed-299c-4923-a429-c9ae96960d77', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, '${role_view-applications}', 'view-applications', 'university', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('052a88a8-d34e-4a34-8989-f5dbf1b937b9', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, '${role_view-consent}', 'view-consent', 'university', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('a830571b-918e-40ed-8ed8-e70dfbbd0615', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, '${role_manage-consent}', 'manage-consent', 'university', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('2a0571be-9d82-4a84-9ccf-8438bdcd600b', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', true, '${role_delete-account}', 'delete-account', 'university', '52c64d0a-8f98-4daa-9d63-a438087f2dc2', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('368b145e-b8f6-4506-8497-c7b9fc542d85', '92c10070-548c-4879-be84-b271f89f3223', true, '${role_impersonation}', 'impersonation', 'master', '92c10070-548c-4879-be84-b271f89f3223', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('070f3c45-5544-4b43-95f2-b75f946e1530', '35a328ae-7085-4919-b67b-3deeb7784afc', true, '${role_impersonation}', 'impersonation', 'university', '35a328ae-7085-4919-b67b-3deeb7784afc', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('77a1ec62-7d3f-45c2-9b4c-bfb31fb47d47', '2563eadc-22da-4cf0-b50b-cbae77e3e68e', true, '${role_read-token}', 'read-token', 'university', '2563eadc-22da-4cf0-b50b-cbae77e3e68e', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('5383feb3-2b9a-485c-94a6-f55ec0007667', 'university', false, '${role_offline-access}', 'offline_access', 'university', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('610dc02e-f670-4ba5-a2e8-83144e259cde', 'university', false, '${role_uma_authorization}', 'uma_authorization', 'university', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('c53d2300-11db-4f62-aa7d-0d18da435e1b', 'university', false, 'administration role', 'ADMIN', 'university', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('c57117e1-dbbf-406c-8e4e-1d100911514f', 'university', false, 'standard user with read-only permissions', 'USER', 'university', NULL, NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('1a53fd78-0811-4458-bc39-1aa921bc35d8', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', true, NULL, 'uma_protection', 'university', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('edd949da-dfb2-4f8a-9fe5-a8d52b4e36b8', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', true, NULL, 'USER', 'university', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', NULL);
INSERT INTO public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) VALUES ('b8d2a933-8832-4b7c-8b97-6a947fd8a1cf', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', true, NULL, 'ADMIN', 'university', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', NULL);


--
-- TOC entry 3877 (class 0 OID 16944)
-- Dependencies: 236
-- Data for Name: migration_model; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.migration_model (id, version, update_time) VALUES ('n6tyw', '16.1.0', 1643016917);


--
-- TOC entry 3894 (class 0 OID 17228)
-- Dependencies: 253
-- Data for Name: offline_client_session; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3893 (class 0 OID 17222)
-- Dependencies: 252
-- Data for Name: offline_user_session; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3907 (class 0 OID 17451)
-- Dependencies: 266
-- Data for Name: policy_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3869 (class 0 OID 16812)
-- Dependencies: 228
-- Data for Name: protocol_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('75da25a4-322c-4c74-b258-7088d2ed26ff', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', '37288d76-bdb6-4a6b-853a-cdd8787f34e0', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('dc30b913-5bfd-489f-a3a8-42a8d4f7c315', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', '0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('f82d1338-2f6c-4612-87fa-ddb96a3e9b49', 'role list', 'saml', 'saml-role-list-mapper', NULL, 'c18c5ada-6579-4a8c-ab75-0f23f563a2b6');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('a8bcda2d-9dea-4dab-a2d3-f4df24346b3c', 'full name', 'openid-connect', 'oidc-full-name-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('750df1b8-e009-4c8a-866a-7cade7c241ef', 'family name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('5c761775-209e-4ee0-81b7-8785c01fc534', 'given name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('5b571887-757d-4416-bc39-50aa06da086f', 'middle name', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('c60906f4-3722-4262-b050-0f947801e1db', 'nickname', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('b2553b8c-2089-49db-b5b0-3b117f577331', 'username', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('b113f4bb-5dd5-4c7f-b098-92f6557defd8', 'profile', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('463902d6-c013-4637-9fb6-2e0d984aa2f1', 'picture', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('b3638795-dfa8-4269-b32e-e83c753b772d', 'website', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('02b39c75-a3f7-45a9-91b5-7c731fa194fd', 'gender', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('0675805e-c9f6-4fb6-bcda-0c99c9e5fbe4', 'birthdate', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('dd09bca3-05f1-4866-a615-a03fce142bd6', 'zoneinfo', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('0fe410a0-0df8-45ff-8687-585e629adacf', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('279c9478-7b3f-4ffc-b1f6-1c48d2e745de', 'updated at', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'ec4a6960-c69a-44b3-aa6d-b7e368f38ae0');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('fcf46a0d-8f29-495a-b5fb-021368f4ab5d', 'email', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '0cef8854-46a9-4dc4-98c1-35b9d10d5a59');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('c645c005-775e-4dd5-ae77-53ae7b1ec1e4', 'email verified', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '0cef8854-46a9-4dc4-98c1-35b9d10d5a59');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'address', 'openid-connect', 'oidc-address-mapper', NULL, 'd3905410-7155-4fad-a2a5-5155f64dee8a');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('f015f8d4-92dd-436f-a63e-1b7ba320e051', 'phone number', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '45b07521-a8a3-466c-a36d-ada9717ed9b4');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('719f2d31-46f4-42de-a026-ab7f2e85f707', 'phone number verified', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '45b07521-a8a3-466c-a36d-ada9717ed9b4');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('4eafb0b4-a833-4d42-af83-0fea17835118', 'realm roles', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, '38de0fa7-5b2b-4cb9-921c-0f16a124eed1');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('6539bcfa-0f67-4ede-8071-4142128d2a7a', 'client roles', 'openid-connect', 'oidc-usermodel-client-role-mapper', NULL, '38de0fa7-5b2b-4cb9-921c-0f16a124eed1');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('00588f1b-a415-4b24-b65e-d431e2ee097d', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', NULL, '38de0fa7-5b2b-4cb9-921c-0f16a124eed1');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('e8a7cd68-ea47-4918-aa13-f6c23df483b8', 'allowed web origins', 'openid-connect', 'oidc-allowed-origins-mapper', NULL, 'd2708b0a-4401-4da9-abea-9ca33e2cd8db');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('e4f42929-287d-4f33-a100-2f771ad48d1c', 'upn', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'f3ed45d6-a944-4af5-aeb4-79291fabc9be');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('7a0dbca2-4906-4544-973c-fe1db529fec0', 'groups', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, 'f3ed45d6-a944-4af5-aeb4-79291fabc9be');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('76b74783-56db-430a-a76d-6d340df233da', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', 'c7a5ef52-81ae-40c3-b9a3-21994ecec08d', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('25a0c810-f1a2-420a-9329-1bc2dd8e327d', 'role list', 'saml', 'saml-role-list-mapper', NULL, '057389b7-6253-43d7-ac0a-a57105bbf4e5');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('35ebe60b-c97c-4760-ba7f-df7eb13c7591', 'full name', 'openid-connect', 'oidc-full-name-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('21a258a4-038e-4729-94d4-7adf25f30fe4', 'family name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('9bd02b2c-9c3e-432a-8234-7cf5593cdc3f', 'given name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('34bfcce9-b886-43d2-922a-235f33995c66', 'middle name', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('2f55ef84-e002-43c4-bdcb-6bb85a8b468a', 'nickname', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('7ec6db01-d9c5-439a-b618-8cd23ef43b96', 'username', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('29f903af-5055-42d8-998a-fe45d4a61b7f', 'profile', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('9222e072-9217-4ed0-9c10-290f8d169f80', 'picture', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('24733813-db0c-4d00-83b8-6c7d4a9d69ba', 'website', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('f7f33df2-c470-4158-a36c-757db339ce7c', 'gender', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('9334b263-cf73-4240-8e0f-41fe7d729092', 'birthdate', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('7f42bd38-63f3-4f53-a159-04aac2015569', 'zoneinfo', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('ca37f8f4-a7e3-4b69-89a9-6aed2742e8ab', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('a99a03ec-9ab7-4592-9474-710bb132994f', 'updated at', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '18a5ab32-7d92-4063-8a40-e4859ef31db7');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('39582012-b6c2-414e-9640-6593b6bdd3f6', 'email', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'a3337170-3392-413a-8d20-a441e0f70c2d');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('78dfb657-2e43-4c8b-9bfa-c7bdea8fede2', 'email verified', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'a3337170-3392-413a-8d20-a441e0f70c2d');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'address', 'openid-connect', 'oidc-address-mapper', NULL, '641d3330-8b5e-4d73-963d-99cbe7c60ce4');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('a3004a1e-7b0c-44de-8caa-2120ecc09f02', 'phone number', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'c23e5d45-4fe9-467c-b1e3-dad6763cb119');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('c4796551-8b90-441b-bfcf-ec2abe6972e0', 'phone number verified', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'c23e5d45-4fe9-467c-b1e3-dad6763cb119');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('6ed3607c-aabe-4f30-9e5d-5fc964340c15', 'realm roles', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, '6a47123f-6028-438d-9a9e-cb9ad77f4104');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('26b2b349-56a0-4e2d-8d00-d448f86be460', 'client roles', 'openid-connect', 'oidc-usermodel-client-role-mapper', NULL, '6a47123f-6028-438d-9a9e-cb9ad77f4104');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('5d1a1a10-efc6-49ee-93fe-1c8cd0d5a7f2', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', NULL, '6a47123f-6028-438d-9a9e-cb9ad77f4104');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('ccaf1094-b3a0-44b9-8c65-176b6f192d60', 'allowed web origins', 'openid-connect', 'oidc-allowed-origins-mapper', NULL, '32aa58a7-b3c2-4cf5-9be3-487ccd16aa85');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('09621637-ac73-40b1-92d0-2fa305f398f0', 'upn', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'ea3fea9a-40e1-40fb-ba9c-da535647e706');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('a6428fee-3bf8-4cb9-9f51-28ac2a997d84', 'groups', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, 'ea3fea9a-40e1-40fb-ba9c-da535647e706');
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('5b59a33d-de83-45df-900a-7dd848e54271', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', 'd6176e7d-5390-41da-b0ba-e8f4738ac7a1', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('ec7b99f2-4793-4f2c-8bce-fe3eafef84c8', 'Client ID', 'openid-connect', 'oidc-usersessionmodel-note-mapper', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('3efe725f-eac0-404c-9886-ede27d2fc0dc', 'Client Host', 'openid-connect', 'oidc-usersessionmodel-note-mapper', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('1499737c-f931-4313-b273-f2ed3db1ab11', 'Client IP Address', 'openid-connect', 'oidc-usersessionmodel-note-mapper', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'client roles', 'openid-connect', 'oidc-usermodel-client-role-mapper', '5adbf589-5b13-4cb3-bf67-65f2663f32a3', NULL);
INSERT INTO public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) VALUES ('daec308c-66e9-402f-a0c9-46aa7461342e', 'client roles', 'openid-connect', 'oidc-usermodel-client-role-mapper', NULL, '418e9be3-8b8a-4542-8b9f-f965312bb7cc');


--
-- TOC entry 3870 (class 0 OID 16819)
-- Dependencies: 229
-- Data for Name: protocol_mapper_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dc30b913-5bfd-489f-a3a8-42a8d4f7c315', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dc30b913-5bfd-489f-a3a8-42a8d4f7c315', 'locale', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dc30b913-5bfd-489f-a3a8-42a8d4f7c315', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dc30b913-5bfd-489f-a3a8-42a8d4f7c315', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dc30b913-5bfd-489f-a3a8-42a8d4f7c315', 'locale', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dc30b913-5bfd-489f-a3a8-42a8d4f7c315', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f82d1338-2f6c-4612-87fa-ddb96a3e9b49', 'false', 'single');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f82d1338-2f6c-4612-87fa-ddb96a3e9b49', 'Basic', 'attribute.nameformat');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f82d1338-2f6c-4612-87fa-ddb96a3e9b49', 'Role', 'attribute.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a8bcda2d-9dea-4dab-a2d3-f4df24346b3c', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a8bcda2d-9dea-4dab-a2d3-f4df24346b3c', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a8bcda2d-9dea-4dab-a2d3-f4df24346b3c', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('750df1b8-e009-4c8a-866a-7cade7c241ef', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('750df1b8-e009-4c8a-866a-7cade7c241ef', 'lastName', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('750df1b8-e009-4c8a-866a-7cade7c241ef', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('750df1b8-e009-4c8a-866a-7cade7c241ef', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('750df1b8-e009-4c8a-866a-7cade7c241ef', 'family_name', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('750df1b8-e009-4c8a-866a-7cade7c241ef', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5c761775-209e-4ee0-81b7-8785c01fc534', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5c761775-209e-4ee0-81b7-8785c01fc534', 'firstName', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5c761775-209e-4ee0-81b7-8785c01fc534', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5c761775-209e-4ee0-81b7-8785c01fc534', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5c761775-209e-4ee0-81b7-8785c01fc534', 'given_name', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5c761775-209e-4ee0-81b7-8785c01fc534', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b571887-757d-4416-bc39-50aa06da086f', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b571887-757d-4416-bc39-50aa06da086f', 'middleName', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b571887-757d-4416-bc39-50aa06da086f', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b571887-757d-4416-bc39-50aa06da086f', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b571887-757d-4416-bc39-50aa06da086f', 'middle_name', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b571887-757d-4416-bc39-50aa06da086f', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c60906f4-3722-4262-b050-0f947801e1db', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c60906f4-3722-4262-b050-0f947801e1db', 'nickname', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c60906f4-3722-4262-b050-0f947801e1db', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c60906f4-3722-4262-b050-0f947801e1db', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c60906f4-3722-4262-b050-0f947801e1db', 'nickname', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c60906f4-3722-4262-b050-0f947801e1db', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b2553b8c-2089-49db-b5b0-3b117f577331', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b2553b8c-2089-49db-b5b0-3b117f577331', 'username', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b2553b8c-2089-49db-b5b0-3b117f577331', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b2553b8c-2089-49db-b5b0-3b117f577331', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b2553b8c-2089-49db-b5b0-3b117f577331', 'preferred_username', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b2553b8c-2089-49db-b5b0-3b117f577331', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b113f4bb-5dd5-4c7f-b098-92f6557defd8', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b113f4bb-5dd5-4c7f-b098-92f6557defd8', 'profile', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b113f4bb-5dd5-4c7f-b098-92f6557defd8', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b113f4bb-5dd5-4c7f-b098-92f6557defd8', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b113f4bb-5dd5-4c7f-b098-92f6557defd8', 'profile', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b113f4bb-5dd5-4c7f-b098-92f6557defd8', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('463902d6-c013-4637-9fb6-2e0d984aa2f1', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('463902d6-c013-4637-9fb6-2e0d984aa2f1', 'picture', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('463902d6-c013-4637-9fb6-2e0d984aa2f1', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('463902d6-c013-4637-9fb6-2e0d984aa2f1', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('463902d6-c013-4637-9fb6-2e0d984aa2f1', 'picture', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('463902d6-c013-4637-9fb6-2e0d984aa2f1', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b3638795-dfa8-4269-b32e-e83c753b772d', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b3638795-dfa8-4269-b32e-e83c753b772d', 'website', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b3638795-dfa8-4269-b32e-e83c753b772d', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b3638795-dfa8-4269-b32e-e83c753b772d', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b3638795-dfa8-4269-b32e-e83c753b772d', 'website', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('b3638795-dfa8-4269-b32e-e83c753b772d', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('02b39c75-a3f7-45a9-91b5-7c731fa194fd', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('02b39c75-a3f7-45a9-91b5-7c731fa194fd', 'gender', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('02b39c75-a3f7-45a9-91b5-7c731fa194fd', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('02b39c75-a3f7-45a9-91b5-7c731fa194fd', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('02b39c75-a3f7-45a9-91b5-7c731fa194fd', 'gender', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('02b39c75-a3f7-45a9-91b5-7c731fa194fd', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0675805e-c9f6-4fb6-bcda-0c99c9e5fbe4', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0675805e-c9f6-4fb6-bcda-0c99c9e5fbe4', 'birthdate', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0675805e-c9f6-4fb6-bcda-0c99c9e5fbe4', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0675805e-c9f6-4fb6-bcda-0c99c9e5fbe4', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0675805e-c9f6-4fb6-bcda-0c99c9e5fbe4', 'birthdate', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0675805e-c9f6-4fb6-bcda-0c99c9e5fbe4', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dd09bca3-05f1-4866-a615-a03fce142bd6', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dd09bca3-05f1-4866-a615-a03fce142bd6', 'zoneinfo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dd09bca3-05f1-4866-a615-a03fce142bd6', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dd09bca3-05f1-4866-a615-a03fce142bd6', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dd09bca3-05f1-4866-a615-a03fce142bd6', 'zoneinfo', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('dd09bca3-05f1-4866-a615-a03fce142bd6', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0fe410a0-0df8-45ff-8687-585e629adacf', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0fe410a0-0df8-45ff-8687-585e629adacf', 'locale', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0fe410a0-0df8-45ff-8687-585e629adacf', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0fe410a0-0df8-45ff-8687-585e629adacf', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0fe410a0-0df8-45ff-8687-585e629adacf', 'locale', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('0fe410a0-0df8-45ff-8687-585e629adacf', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('279c9478-7b3f-4ffc-b1f6-1c48d2e745de', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('279c9478-7b3f-4ffc-b1f6-1c48d2e745de', 'updatedAt', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('279c9478-7b3f-4ffc-b1f6-1c48d2e745de', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('279c9478-7b3f-4ffc-b1f6-1c48d2e745de', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('279c9478-7b3f-4ffc-b1f6-1c48d2e745de', 'updated_at', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('279c9478-7b3f-4ffc-b1f6-1c48d2e745de', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('fcf46a0d-8f29-495a-b5fb-021368f4ab5d', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('fcf46a0d-8f29-495a-b5fb-021368f4ab5d', 'email', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('fcf46a0d-8f29-495a-b5fb-021368f4ab5d', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('fcf46a0d-8f29-495a-b5fb-021368f4ab5d', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('fcf46a0d-8f29-495a-b5fb-021368f4ab5d', 'email', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('fcf46a0d-8f29-495a-b5fb-021368f4ab5d', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c645c005-775e-4dd5-ae77-53ae7b1ec1e4', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c645c005-775e-4dd5-ae77-53ae7b1ec1e4', 'emailVerified', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c645c005-775e-4dd5-ae77-53ae7b1ec1e4', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c645c005-775e-4dd5-ae77-53ae7b1ec1e4', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c645c005-775e-4dd5-ae77-53ae7b1ec1e4', 'email_verified', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c645c005-775e-4dd5-ae77-53ae7b1ec1e4', 'boolean', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'formatted', 'user.attribute.formatted');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'country', 'user.attribute.country');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'postal_code', 'user.attribute.postal_code');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'street', 'user.attribute.street');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'region', 'user.attribute.region');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4652c9c7-4e4e-4dc9-8800-caa4c8b9eb63', 'locality', 'user.attribute.locality');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f015f8d4-92dd-436f-a63e-1b7ba320e051', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f015f8d4-92dd-436f-a63e-1b7ba320e051', 'phoneNumber', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f015f8d4-92dd-436f-a63e-1b7ba320e051', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f015f8d4-92dd-436f-a63e-1b7ba320e051', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f015f8d4-92dd-436f-a63e-1b7ba320e051', 'phone_number', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f015f8d4-92dd-436f-a63e-1b7ba320e051', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('719f2d31-46f4-42de-a026-ab7f2e85f707', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('719f2d31-46f4-42de-a026-ab7f2e85f707', 'phoneNumberVerified', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('719f2d31-46f4-42de-a026-ab7f2e85f707', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('719f2d31-46f4-42de-a026-ab7f2e85f707', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('719f2d31-46f4-42de-a026-ab7f2e85f707', 'phone_number_verified', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('719f2d31-46f4-42de-a026-ab7f2e85f707', 'boolean', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4eafb0b4-a833-4d42-af83-0fea17835118', 'true', 'multivalued');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4eafb0b4-a833-4d42-af83-0fea17835118', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4eafb0b4-a833-4d42-af83-0fea17835118', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4eafb0b4-a833-4d42-af83-0fea17835118', 'realm_access.roles', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('4eafb0b4-a833-4d42-af83-0fea17835118', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6539bcfa-0f67-4ede-8071-4142128d2a7a', 'true', 'multivalued');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6539bcfa-0f67-4ede-8071-4142128d2a7a', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6539bcfa-0f67-4ede-8071-4142128d2a7a', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6539bcfa-0f67-4ede-8071-4142128d2a7a', 'resource_access.${client_id}.roles', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6539bcfa-0f67-4ede-8071-4142128d2a7a', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('e4f42929-287d-4f33-a100-2f771ad48d1c', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('e4f42929-287d-4f33-a100-2f771ad48d1c', 'username', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('e4f42929-287d-4f33-a100-2f771ad48d1c', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('e4f42929-287d-4f33-a100-2f771ad48d1c', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('e4f42929-287d-4f33-a100-2f771ad48d1c', 'upn', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('e4f42929-287d-4f33-a100-2f771ad48d1c', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7a0dbca2-4906-4544-973c-fe1db529fec0', 'true', 'multivalued');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7a0dbca2-4906-4544-973c-fe1db529fec0', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7a0dbca2-4906-4544-973c-fe1db529fec0', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7a0dbca2-4906-4544-973c-fe1db529fec0', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7a0dbca2-4906-4544-973c-fe1db529fec0', 'groups', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7a0dbca2-4906-4544-973c-fe1db529fec0', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('25a0c810-f1a2-420a-9329-1bc2dd8e327d', 'false', 'single');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('25a0c810-f1a2-420a-9329-1bc2dd8e327d', 'Basic', 'attribute.nameformat');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('25a0c810-f1a2-420a-9329-1bc2dd8e327d', 'Role', 'attribute.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('35ebe60b-c97c-4760-ba7f-df7eb13c7591', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('35ebe60b-c97c-4760-ba7f-df7eb13c7591', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('35ebe60b-c97c-4760-ba7f-df7eb13c7591', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('21a258a4-038e-4729-94d4-7adf25f30fe4', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('21a258a4-038e-4729-94d4-7adf25f30fe4', 'lastName', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('21a258a4-038e-4729-94d4-7adf25f30fe4', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('21a258a4-038e-4729-94d4-7adf25f30fe4', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('21a258a4-038e-4729-94d4-7adf25f30fe4', 'family_name', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('21a258a4-038e-4729-94d4-7adf25f30fe4', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9bd02b2c-9c3e-432a-8234-7cf5593cdc3f', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9bd02b2c-9c3e-432a-8234-7cf5593cdc3f', 'firstName', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9bd02b2c-9c3e-432a-8234-7cf5593cdc3f', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9bd02b2c-9c3e-432a-8234-7cf5593cdc3f', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9bd02b2c-9c3e-432a-8234-7cf5593cdc3f', 'given_name', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9bd02b2c-9c3e-432a-8234-7cf5593cdc3f', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('34bfcce9-b886-43d2-922a-235f33995c66', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('34bfcce9-b886-43d2-922a-235f33995c66', 'middleName', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('34bfcce9-b886-43d2-922a-235f33995c66', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('34bfcce9-b886-43d2-922a-235f33995c66', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('34bfcce9-b886-43d2-922a-235f33995c66', 'middle_name', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('34bfcce9-b886-43d2-922a-235f33995c66', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('2f55ef84-e002-43c4-bdcb-6bb85a8b468a', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('2f55ef84-e002-43c4-bdcb-6bb85a8b468a', 'nickname', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('2f55ef84-e002-43c4-bdcb-6bb85a8b468a', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('2f55ef84-e002-43c4-bdcb-6bb85a8b468a', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('2f55ef84-e002-43c4-bdcb-6bb85a8b468a', 'nickname', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('2f55ef84-e002-43c4-bdcb-6bb85a8b468a', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7ec6db01-d9c5-439a-b618-8cd23ef43b96', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7ec6db01-d9c5-439a-b618-8cd23ef43b96', 'username', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7ec6db01-d9c5-439a-b618-8cd23ef43b96', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7ec6db01-d9c5-439a-b618-8cd23ef43b96', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7ec6db01-d9c5-439a-b618-8cd23ef43b96', 'preferred_username', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7ec6db01-d9c5-439a-b618-8cd23ef43b96', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('29f903af-5055-42d8-998a-fe45d4a61b7f', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('29f903af-5055-42d8-998a-fe45d4a61b7f', 'profile', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('29f903af-5055-42d8-998a-fe45d4a61b7f', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('29f903af-5055-42d8-998a-fe45d4a61b7f', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('29f903af-5055-42d8-998a-fe45d4a61b7f', 'profile', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('29f903af-5055-42d8-998a-fe45d4a61b7f', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9222e072-9217-4ed0-9c10-290f8d169f80', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9222e072-9217-4ed0-9c10-290f8d169f80', 'picture', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9222e072-9217-4ed0-9c10-290f8d169f80', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9222e072-9217-4ed0-9c10-290f8d169f80', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9222e072-9217-4ed0-9c10-290f8d169f80', 'picture', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9222e072-9217-4ed0-9c10-290f8d169f80', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('24733813-db0c-4d00-83b8-6c7d4a9d69ba', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('24733813-db0c-4d00-83b8-6c7d4a9d69ba', 'website', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('24733813-db0c-4d00-83b8-6c7d4a9d69ba', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('24733813-db0c-4d00-83b8-6c7d4a9d69ba', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('24733813-db0c-4d00-83b8-6c7d4a9d69ba', 'website', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('24733813-db0c-4d00-83b8-6c7d4a9d69ba', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f7f33df2-c470-4158-a36c-757db339ce7c', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f7f33df2-c470-4158-a36c-757db339ce7c', 'gender', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f7f33df2-c470-4158-a36c-757db339ce7c', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f7f33df2-c470-4158-a36c-757db339ce7c', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f7f33df2-c470-4158-a36c-757db339ce7c', 'gender', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('f7f33df2-c470-4158-a36c-757db339ce7c', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9334b263-cf73-4240-8e0f-41fe7d729092', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9334b263-cf73-4240-8e0f-41fe7d729092', 'birthdate', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9334b263-cf73-4240-8e0f-41fe7d729092', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9334b263-cf73-4240-8e0f-41fe7d729092', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9334b263-cf73-4240-8e0f-41fe7d729092', 'birthdate', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9334b263-cf73-4240-8e0f-41fe7d729092', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7f42bd38-63f3-4f53-a159-04aac2015569', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7f42bd38-63f3-4f53-a159-04aac2015569', 'zoneinfo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7f42bd38-63f3-4f53-a159-04aac2015569', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7f42bd38-63f3-4f53-a159-04aac2015569', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7f42bd38-63f3-4f53-a159-04aac2015569', 'zoneinfo', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('7f42bd38-63f3-4f53-a159-04aac2015569', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ca37f8f4-a7e3-4b69-89a9-6aed2742e8ab', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ca37f8f4-a7e3-4b69-89a9-6aed2742e8ab', 'locale', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ca37f8f4-a7e3-4b69-89a9-6aed2742e8ab', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ca37f8f4-a7e3-4b69-89a9-6aed2742e8ab', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ca37f8f4-a7e3-4b69-89a9-6aed2742e8ab', 'locale', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ca37f8f4-a7e3-4b69-89a9-6aed2742e8ab', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a99a03ec-9ab7-4592-9474-710bb132994f', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a99a03ec-9ab7-4592-9474-710bb132994f', 'updatedAt', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a99a03ec-9ab7-4592-9474-710bb132994f', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a99a03ec-9ab7-4592-9474-710bb132994f', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a99a03ec-9ab7-4592-9474-710bb132994f', 'updated_at', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a99a03ec-9ab7-4592-9474-710bb132994f', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('39582012-b6c2-414e-9640-6593b6bdd3f6', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('39582012-b6c2-414e-9640-6593b6bdd3f6', 'email', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('39582012-b6c2-414e-9640-6593b6bdd3f6', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('39582012-b6c2-414e-9640-6593b6bdd3f6', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('39582012-b6c2-414e-9640-6593b6bdd3f6', 'email', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('39582012-b6c2-414e-9640-6593b6bdd3f6', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('78dfb657-2e43-4c8b-9bfa-c7bdea8fede2', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('78dfb657-2e43-4c8b-9bfa-c7bdea8fede2', 'emailVerified', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('78dfb657-2e43-4c8b-9bfa-c7bdea8fede2', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('78dfb657-2e43-4c8b-9bfa-c7bdea8fede2', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('78dfb657-2e43-4c8b-9bfa-c7bdea8fede2', 'email_verified', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('78dfb657-2e43-4c8b-9bfa-c7bdea8fede2', 'boolean', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'formatted', 'user.attribute.formatted');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'country', 'user.attribute.country');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'postal_code', 'user.attribute.postal_code');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'street', 'user.attribute.street');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'region', 'user.attribute.region');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('9ea6a945-a885-433e-ba03-966c084cdf80', 'locality', 'user.attribute.locality');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a3004a1e-7b0c-44de-8caa-2120ecc09f02', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a3004a1e-7b0c-44de-8caa-2120ecc09f02', 'phoneNumber', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a3004a1e-7b0c-44de-8caa-2120ecc09f02', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a3004a1e-7b0c-44de-8caa-2120ecc09f02', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a3004a1e-7b0c-44de-8caa-2120ecc09f02', 'phone_number', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a3004a1e-7b0c-44de-8caa-2120ecc09f02', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c4796551-8b90-441b-bfcf-ec2abe6972e0', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c4796551-8b90-441b-bfcf-ec2abe6972e0', 'phoneNumberVerified', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c4796551-8b90-441b-bfcf-ec2abe6972e0', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c4796551-8b90-441b-bfcf-ec2abe6972e0', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c4796551-8b90-441b-bfcf-ec2abe6972e0', 'phone_number_verified', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('c4796551-8b90-441b-bfcf-ec2abe6972e0', 'boolean', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6ed3607c-aabe-4f30-9e5d-5fc964340c15', 'true', 'multivalued');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6ed3607c-aabe-4f30-9e5d-5fc964340c15', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6ed3607c-aabe-4f30-9e5d-5fc964340c15', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6ed3607c-aabe-4f30-9e5d-5fc964340c15', 'realm_access.roles', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('6ed3607c-aabe-4f30-9e5d-5fc964340c15', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('26b2b349-56a0-4e2d-8d00-d448f86be460', 'true', 'multivalued');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('26b2b349-56a0-4e2d-8d00-d448f86be460', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('26b2b349-56a0-4e2d-8d00-d448f86be460', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('26b2b349-56a0-4e2d-8d00-d448f86be460', 'resource_access.${client_id}.roles', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('26b2b349-56a0-4e2d-8d00-d448f86be460', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('09621637-ac73-40b1-92d0-2fa305f398f0', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('09621637-ac73-40b1-92d0-2fa305f398f0', 'username', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('09621637-ac73-40b1-92d0-2fa305f398f0', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('09621637-ac73-40b1-92d0-2fa305f398f0', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('09621637-ac73-40b1-92d0-2fa305f398f0', 'upn', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('09621637-ac73-40b1-92d0-2fa305f398f0', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a6428fee-3bf8-4cb9-9f51-28ac2a997d84', 'true', 'multivalued');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a6428fee-3bf8-4cb9-9f51-28ac2a997d84', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a6428fee-3bf8-4cb9-9f51-28ac2a997d84', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a6428fee-3bf8-4cb9-9f51-28ac2a997d84', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a6428fee-3bf8-4cb9-9f51-28ac2a997d84', 'groups', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('a6428fee-3bf8-4cb9-9f51-28ac2a997d84', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b59a33d-de83-45df-900a-7dd848e54271', 'true', 'userinfo.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b59a33d-de83-45df-900a-7dd848e54271', 'locale', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b59a33d-de83-45df-900a-7dd848e54271', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b59a33d-de83-45df-900a-7dd848e54271', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b59a33d-de83-45df-900a-7dd848e54271', 'locale', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('5b59a33d-de83-45df-900a-7dd848e54271', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ec7b99f2-4793-4f2c-8bce-fe3eafef84c8', 'clientId', 'user.session.note');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ec7b99f2-4793-4f2c-8bce-fe3eafef84c8', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ec7b99f2-4793-4f2c-8bce-fe3eafef84c8', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ec7b99f2-4793-4f2c-8bce-fe3eafef84c8', 'clientId', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('ec7b99f2-4793-4f2c-8bce-fe3eafef84c8', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('3efe725f-eac0-404c-9886-ede27d2fc0dc', 'clientHost', 'user.session.note');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('3efe725f-eac0-404c-9886-ede27d2fc0dc', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('3efe725f-eac0-404c-9886-ede27d2fc0dc', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('3efe725f-eac0-404c-9886-ede27d2fc0dc', 'clientHost', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('3efe725f-eac0-404c-9886-ede27d2fc0dc', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('1499737c-f931-4313-b273-f2ed3db1ab11', 'clientAddress', 'user.session.note');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('1499737c-f931-4313-b273-f2ed3db1ab11', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('1499737c-f931-4313-b273-f2ed3db1ab11', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('1499737c-f931-4313-b273-f2ed3db1ab11', 'clientAddress', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('1499737c-f931-4313-b273-f2ed3db1ab11', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'resource_access.${client_id}.roles', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'true', 'id.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'university_client', 'usermodel.clientRoleMapping.clientId');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('daec308c-66e9-402f-a0c9-46aa7461342e', 'foo', 'user.attribute');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('daec308c-66e9-402f-a0c9-46aa7461342e', 'true', 'access.token.claim');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('daec308c-66e9-402f-a0c9-46aa7461342e', 'resource_access.${client_id}.roles', 'claim.name');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('daec308c-66e9-402f-a0c9-46aa7461342e', 'String', 'jsonType.label');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('daec308c-66e9-402f-a0c9-46aa7461342e', 'true', 'multivalued');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('daec308c-66e9-402f-a0c9-46aa7461342e', 'university_client', 'usermodel.clientRoleMapping.clientId');
INSERT INTO public.protocol_mapper_config (protocol_mapper_id, value, name) VALUES ('8c7e38e3-4f3b-4ea2-978e-58af50951f86', 'false', 'multivalued');


--
-- TOC entry 3850 (class 0 OID 16447)
-- Dependencies: 209
-- Data for Name: realm; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.realm (id, access_code_lifespan, user_action_lifespan, access_token_lifespan, account_theme, admin_theme, email_theme, enabled, events_enabled, events_expiration, login_theme, name, not_before, password_policy, registration_allowed, remember_me, reset_password_allowed, social, ssl_required, sso_idle_timeout, sso_max_lifespan, update_profile_on_soc_login, verify_email, master_admin_client, login_lifespan, internationalization_enabled, default_locale, reg_email_as_username, admin_events_enabled, admin_events_details_enabled, edit_username_allowed, otp_policy_counter, otp_policy_window, otp_policy_period, otp_policy_digits, otp_policy_alg, otp_policy_type, browser_flow, registration_flow, direct_grant_flow, reset_credentials_flow, client_auth_flow, offline_session_idle_timeout, revoke_refresh_token, access_token_life_implicit, login_with_email_allowed, duplicate_emails_allowed, docker_auth_flow, refresh_token_max_reuse, allow_user_managed_access, sso_max_lifespan_remember_me, sso_idle_timeout_remember_me, default_role) VALUES ('university', 60, 300, 300, 'keycloak', NULL, NULL, true, false, 0, 'keycloak', 'university', 0, NULL, false, false, false, false, 'NONE', 1800, 36000, false, false, '92c10070-548c-4879-be84-b271f89f3223', 1800, false, 'en', false, false, false, false, 0, 1, 30, 6, 'HmacSHA1', 'totp', '52558793-ede5-4c63-bb75-e315dc679d5b', '489811e4-a562-4d76-b0ae-5426ec57001b', '68821da6-a750-490d-b70a-a7db66e8cf7f', 'bba4b5f3-2103-44a4-84ef-a7d9da97cc3a', '6f779007-976d-4020-8b43-ded2b8b65a72', 2592000, false, 900, false, false, 'bf3b9371-4ea3-4134-9462-99c4080c486c', 0, false, 0, 0, '0d4b0f93-41a0-4794-871e-8fa2cd402992');
INSERT INTO public.realm (id, access_code_lifespan, user_action_lifespan, access_token_lifespan, account_theme, admin_theme, email_theme, enabled, events_enabled, events_expiration, login_theme, name, not_before, password_policy, registration_allowed, remember_me, reset_password_allowed, social, ssl_required, sso_idle_timeout, sso_max_lifespan, update_profile_on_soc_login, verify_email, master_admin_client, login_lifespan, internationalization_enabled, default_locale, reg_email_as_username, admin_events_enabled, admin_events_details_enabled, edit_username_allowed, otp_policy_counter, otp_policy_window, otp_policy_period, otp_policy_digits, otp_policy_alg, otp_policy_type, browser_flow, registration_flow, direct_grant_flow, reset_credentials_flow, client_auth_flow, offline_session_idle_timeout, revoke_refresh_token, access_token_life_implicit, login_with_email_allowed, duplicate_emails_allowed, docker_auth_flow, refresh_token_max_reuse, allow_user_managed_access, sso_max_lifespan_remember_me, sso_idle_timeout_remember_me, default_role) VALUES ('master', 60, 300, 60, NULL, NULL, NULL, true, false, 0, NULL, 'master', 0, NULL, false, false, false, false, 'EXTERNAL', 1800, 36000, false, false, '5a51f87d-6c9d-400c-bb6a-7d028ec2be89', 1800, false, NULL, false, false, false, false, 0, 1, 30, 6, 'HmacSHA1', 'totp', 'e6b99665-8fdd-485e-b907-0a560125c614', 'd28e8a0f-a23b-4d73-bc51-ba3943d430d6', 'e8070c89-1270-4be4-9088-6806e74a62f2', '921fc4a2-d27b-4bde-99e3-4f27eb7d989a', '020d98e6-cd98-41bf-9bb0-d6059d0b61e9', 2592000, false, 900, true, false, '3a60391a-815b-4f4e-86a8-6e7159c76e9a', 0, false, 0, 0, 'd58f3db5-7572-41c6-b871-530e48d3ab60');


--
-- TOC entry 3851 (class 0 OID 16465)
-- Dependencies: 210
-- Data for Name: realm_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.contentSecurityPolicyReportOnly', 'master', '');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xContentTypeOptions', 'master', 'nosniff');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xRobotsTag', 'master', 'none');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xFrameOptions', 'master', 'SAMEORIGIN');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.contentSecurityPolicy', 'master', 'frame-src ''self''; frame-ancestors ''self''; object-src ''none'';');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xXSSProtection', 'master', '1; mode=block');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.strictTransportSecurity', 'master', 'max-age=31536000; includeSubDomains');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('bruteForceProtected', 'master', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('permanentLockout', 'master', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('maxFailureWaitSeconds', 'master', '900');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('minimumQuickLoginWaitSeconds', 'master', '60');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('waitIncrementSeconds', 'master', '60');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('quickLoginCheckMilliSeconds', 'master', '1000');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('maxDeltaTimeSeconds', 'master', '43200');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('failureFactor', 'master', '30');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('displayName', 'master', 'Keycloak');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('displayNameHtml', 'master', '<div class="kc-logo-text"><span>Keycloak</span></div>');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('defaultSignatureAlgorithm', 'master', 'RS256');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('offlineSessionMaxLifespanEnabled', 'master', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('offlineSessionMaxLifespan', 'master', '5184000');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('oauth2DeviceCodeLifespan', 'university', '600');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('oauth2DevicePollingInterval', 'university', '5');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('cibaBackchannelTokenDeliveryMode', 'university', 'poll');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('cibaExpiresIn', 'university', '120');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('cibaInterval', 'university', '5');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('cibaAuthRequestedUserHint', 'university', 'login_hint');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('parRequestUriLifespan', 'university', '60');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('clientSessionIdleTimeout', 'university', '0');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('clientSessionMaxLifespan', 'university', '0');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('clientOfflineSessionIdleTimeout', 'university', '0');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('clientOfflineSessionMaxLifespan', 'university', '0');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('bruteForceProtected', 'university', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('permanentLockout', 'university', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('maxFailureWaitSeconds', 'university', '900');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('minimumQuickLoginWaitSeconds', 'university', '60');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('waitIncrementSeconds', 'university', '60');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('quickLoginCheckMilliSeconds', 'university', '1000');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('maxDeltaTimeSeconds', 'university', '43200');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('failureFactor', 'university', '30');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('actionTokenGeneratedByAdminLifespan', 'university', '43200');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('actionTokenGeneratedByUserLifespan', 'university', '300');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('defaultSignatureAlgorithm', 'university', 'RS256');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('offlineSessionMaxLifespanEnabled', 'university', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('offlineSessionMaxLifespan', 'university', '5184000');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyRpEntityName', 'university', 'keycloak');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicySignatureAlgorithms', 'university', 'ES256');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyRpId', 'university', '');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyAttestationConveyancePreference', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyAuthenticatorAttachment', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyRequireResidentKey', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyUserVerificationRequirement', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyCreateTimeout', 'university', '0');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyAvoidSameAuthenticatorRegister', 'university', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyRpEntityNamePasswordless', 'university', 'keycloak');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicySignatureAlgorithmsPasswordless', 'university', 'ES256');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyRpIdPasswordless', 'university', '');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyAttestationConveyancePreferencePasswordless', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyAuthenticatorAttachmentPasswordless', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyRequireResidentKeyPasswordless', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyUserVerificationRequirementPasswordless', 'university', 'not specified');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyCreateTimeoutPasswordless', 'university', '0');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('webAuthnPolicyAvoidSameAuthenticatorRegisterPasswordless', 'university', 'false');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('client-policies.profiles', 'university', '{"profiles":[]}');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('client-policies.policies', 'university', '{"policies":[]}');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.contentSecurityPolicyReportOnly', 'university', '');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xContentTypeOptions', 'university', 'nosniff');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xRobotsTag', 'university', 'none');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xFrameOptions', 'university', 'SAMEORIGIN');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.contentSecurityPolicy', 'university', 'frame-src ''self''; frame-ancestors ''self''; object-src ''none'';');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.xXSSProtection', 'university', '1; mode=block');
INSERT INTO public.realm_attribute (name, realm_id, value) VALUES ('_browser_header.strictTransportSecurity', 'university', 'max-age=31536000; includeSubDomains');


--
-- TOC entry 3899 (class 0 OID 17254)
-- Dependencies: 258
-- Data for Name: realm_default_groups; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3876 (class 0 OID 16936)
-- Dependencies: 235
-- Data for Name: realm_enabled_event_types; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3852 (class 0 OID 16474)
-- Dependencies: 211
-- Data for Name: realm_events_listeners; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.realm_events_listeners (realm_id, value) VALUES ('master', 'jboss-logging');
INSERT INTO public.realm_events_listeners (realm_id, value) VALUES ('university', 'jboss-logging');


--
-- TOC entry 3932 (class 0 OID 17981)
-- Dependencies: 291
-- Data for Name: realm_localizations; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3853 (class 0 OID 16477)
-- Dependencies: 212
-- Data for Name: realm_required_credential; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.realm_required_credential (type, form_label, input, secret, realm_id) VALUES ('password', 'password', true, true, 'master');
INSERT INTO public.realm_required_credential (type, form_label, input, secret, realm_id) VALUES ('password', 'password', true, true, 'university');


--
-- TOC entry 3854 (class 0 OID 16485)
-- Dependencies: 213
-- Data for Name: realm_smtp_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3874 (class 0 OID 16851)
-- Dependencies: 233
-- Data for Name: realm_supported_locales; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'de');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'no');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'ru');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'sv');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'pt-BR');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'lt');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'en');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'it');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'fr');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'hu');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'zh-CN');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'es');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'cs');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'ja');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'sk');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'pl');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'da');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'ca');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'nl');
INSERT INTO public.realm_supported_locales (realm_id, value) VALUES ('university', 'tr');


--
-- TOC entry 3855 (class 0 OID 16497)
-- Dependencies: 214
-- Data for Name: redirect_uris; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.redirect_uris (client_id, value) VALUES ('ab880e32-c897-43c4-bea8-78637e23b4ec', '/realms/master/account/*');
INSERT INTO public.redirect_uris (client_id, value) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', '/realms/master/account/*');
INSERT INTO public.redirect_uris (client_id, value) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', '/admin/master/console/*');
INSERT INTO public.redirect_uris (client_id, value) VALUES ('52c64d0a-8f98-4daa-9d63-a438087f2dc2', '/realms/university/account/*');
INSERT INTO public.redirect_uris (client_id, value) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', '/realms/university/account/*');
INSERT INTO public.redirect_uris (client_id, value) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', '/admin/university/console/*');
INSERT INTO public.redirect_uris (client_id, value) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', '/*');


--
-- TOC entry 3892 (class 0 OID 17185)
-- Dependencies: 251
-- Data for Name: required_action_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3891 (class 0 OID 17177)
-- Dependencies: 250
-- Data for Name: required_action_provider; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('1c352a6b-061d-426e-ba30-7bb5dd2383e5', 'VERIFY_EMAIL', 'Verify Email', 'master', true, false, 'VERIFY_EMAIL', 50);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('1c8f1cbb-fc19-41b1-b9ae-0715419df5a1', 'UPDATE_PROFILE', 'Update Profile', 'master', true, false, 'UPDATE_PROFILE', 40);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('b0e105a3-1737-4af9-81d0-c2efc9c99fe4', 'CONFIGURE_TOTP', 'Configure OTP', 'master', true, false, 'CONFIGURE_TOTP', 10);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('cd44b72e-5f85-4796-81ff-de30ed64e43c', 'UPDATE_PASSWORD', 'Update Password', 'master', true, false, 'UPDATE_PASSWORD', 30);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('7e1a50f7-dc33-4779-8b90-d19cd7f1839f', 'terms_and_conditions', 'Terms and Conditions', 'master', false, false, 'terms_and_conditions', 20);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('94c2a362-35e8-4aaa-bdcc-0541f6f713eb', 'update_user_locale', 'Update User Locale', 'master', true, false, 'update_user_locale', 1000);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('8c880108-314f-465f-a417-c74a1df05593', 'delete_account', 'Delete Account', 'master', false, false, 'delete_account', 60);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('0a02e23f-cf83-40ca-8275-6b36c11095ca', 'VERIFY_EMAIL', 'Verify Email', 'university', true, false, 'VERIFY_EMAIL', 50);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('f6ff7bb0-6053-498a-8e1e-34718def4e50', 'UPDATE_PROFILE', 'Update Profile', 'university', true, false, 'UPDATE_PROFILE', 40);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('bef2ea34-7a1b-46ad-991f-19238dc82081', 'CONFIGURE_TOTP', 'Configure OTP', 'university', true, false, 'CONFIGURE_TOTP', 10);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('5eae6002-8970-486d-b416-35914f5f6e83', 'UPDATE_PASSWORD', 'Update Password', 'university', true, false, 'UPDATE_PASSWORD', 30);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('1837274d-e599-47ef-8987-478512c7dd60', 'terms_and_conditions', 'Terms and Conditions', 'university', false, false, 'terms_and_conditions', 20);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('6fcac9f2-9829-4030-bbc3-9b5e36990877', 'update_user_locale', 'Update User Locale', 'university', true, false, 'update_user_locale', 1000);
INSERT INTO public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) VALUES ('d73cbde5-0330-4f2e-939e-41d3b5abdc39', 'delete_account', 'Delete Account', 'university', false, false, 'delete_account', 60);


--
-- TOC entry 3929 (class 0 OID 17906)
-- Dependencies: 288
-- Data for Name: resource_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3909 (class 0 OID 17479)
-- Dependencies: 268
-- Data for Name: resource_policy; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3908 (class 0 OID 17464)
-- Dependencies: 267
-- Data for Name: resource_scope; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3903 (class 0 OID 17398)
-- Dependencies: 262
-- Data for Name: resource_server; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3928 (class 0 OID 17882)
-- Dependencies: 287
-- Data for Name: resource_server_perm_ticket; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3906 (class 0 OID 17436)
-- Dependencies: 265
-- Data for Name: resource_server_policy; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3904 (class 0 OID 17406)
-- Dependencies: 263
-- Data for Name: resource_server_resource; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3905 (class 0 OID 17421)
-- Dependencies: 264
-- Data for Name: resource_server_scope; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3930 (class 0 OID 17925)
-- Dependencies: 289
-- Data for Name: resource_uris; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3931 (class 0 OID 17935)
-- Dependencies: 290
-- Data for Name: role_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3856 (class 0 OID 16500)
-- Dependencies: 215
-- Data for Name: scope_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.scope_mapping (client_id, role_id) VALUES ('37288d76-bdb6-4a6b-853a-cdd8787f34e0', '62bd7fab-4794-4f6c-a5de-946763965c3b');
INSERT INTO public.scope_mapping (client_id, role_id) VALUES ('c7a5ef52-81ae-40c3-b9a3-21994ecec08d', '197755ea-6368-4aca-a626-813a9d720e11');
INSERT INTO public.scope_mapping (client_id, role_id) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'c57117e1-dbbf-406c-8e4e-1d100911514f');
INSERT INTO public.scope_mapping (client_id, role_id) VALUES ('5adbf589-5b13-4cb3-bf67-65f2663f32a3', 'c53d2300-11db-4f62-aa7d-0d18da435e1b');


--
-- TOC entry 3910 (class 0 OID 17494)
-- Dependencies: 269
-- Data for Name: scope_policy; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3858 (class 0 OID 16506)
-- Dependencies: 217
-- Data for Name: user_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.user_attribute (name, value, user_id, id) VALUES ('role', 'USER', '7b482214-db2e-4ebb-9e42-cc9ba2672e00', '4aabe605-5361-44c9-a0aa-969028e74b04');


--
-- TOC entry 3880 (class 0 OID 16959)
-- Dependencies: 239
-- Data for Name: user_consent; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3926 (class 0 OID 17857)
-- Dependencies: 285
-- Data for Name: user_consent_client_scope; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3859 (class 0 OID 16512)
-- Dependencies: 218
-- Data for Name: user_entity; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.user_entity (id, email, email_constraint, email_verified, enabled, federation_link, first_name, last_name, realm_id, username, created_timestamp, service_account_client_link, not_before) VALUES ('0032b116-627c-4945-b66b-2e61095f9e1b', NULL, '38a79177-925d-4466-b431-384c029dd982', false, true, NULL, NULL, NULL, 'master', 'admin', 1643016919516, NULL, 0);
INSERT INTO public.user_entity (id, email, email_constraint, email_verified, enabled, federation_link, first_name, last_name, realm_id, username, created_timestamp, service_account_client_link, not_before) VALUES ('7cc2b36e-e5e5-415b-a7f4-4fc4bc1ec028', NULL, '61f417c8-2f04-4571-9176-e58785bea5a1', true, true, NULL, '', NULL, 'university', 'admin', 1643018050755, NULL, 0);
INSERT INTO public.user_entity (id, email, email_constraint, email_verified, enabled, federation_link, first_name, last_name, realm_id, username, created_timestamp, service_account_client_link, not_before) VALUES ('7b482214-db2e-4ebb-9e42-cc9ba2672e00', NULL, '8401cb41-9102-4c9b-9630-8fe79209041a', true, true, NULL, 'First Name', 'Last Name', 'university', 'user', 1643018114114, NULL, 1643115197);


--
-- TOC entry 3860 (class 0 OID 16521)
-- Dependencies: 219
-- Data for Name: user_federation_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3887 (class 0 OID 17075)
-- Dependencies: 246
-- Data for Name: user_federation_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3888 (class 0 OID 17081)
-- Dependencies: 247
-- Data for Name: user_federation_mapper_config; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3861 (class 0 OID 16527)
-- Dependencies: 220
-- Data for Name: user_federation_provider; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3898 (class 0 OID 17251)
-- Dependencies: 257
-- Data for Name: user_group_membership; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3862 (class 0 OID 16533)
-- Dependencies: 221
-- Data for Name: user_required_action; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3863 (class 0 OID 16536)
-- Dependencies: 222
-- Data for Name: user_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('d58f3db5-7572-41c6-b871-530e48d3ab60', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('d2cd89ce-b424-448f-8ace-0ba5c8536cd7', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('fce01f44-4fc5-40d8-a341-2c33d6a0b9e6', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('bc8ccc9b-c507-4a28-a0ba-b06648f2c155', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('f8cc984e-0088-472e-83f6-fca0a5e6392b', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('2c10980d-a45a-465f-a5c8-79185070ec04', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('a5895c2c-6c9c-40ab-aff7-c7d114438d6c', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('cabdaa89-998f-4c56-9c98-b62491e582f8', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('bc8668cc-e3cb-47f1-be2f-dfc417bb3b75', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('67859da9-37d7-4673-928d-611c8177e71a', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('bb3435ce-e19d-45fc-8773-a91c8b2bd635', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('56e049c6-cf93-47bc-af49-877f0c1c9945', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('cf871e78-2716-4c81-9ea5-da33841779a3', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('c29cc501-6b1b-4252-bdc1-60d9a7a1c325', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('ec77e898-bb39-4e15-89f4-23f43287af8e', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('bb0472bb-594e-4583-8b88-2f6970109bae', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('e4188e1a-d947-431c-81a4-a2b876f0e4ce', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('3042ec43-5d7f-4e51-b353-08f6db15d78f', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('85980e95-1b7d-4751-9b09-fe95f64f2bdd', '0032b116-627c-4945-b66b-2e61095f9e1b');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('c57117e1-dbbf-406c-8e4e-1d100911514f', '7b482214-db2e-4ebb-9e42-cc9ba2672e00');
INSERT INTO public.user_role_mapping (role_id, user_id) VALUES ('c53d2300-11db-4f62-aa7d-0d18da435e1b', '7cc2b36e-e5e5-415b-a7f4-4fc4bc1ec028');


--
-- TOC entry 3864 (class 0 OID 16539)
-- Dependencies: 223
-- Data for Name: user_session; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3875 (class 0 OID 16854)
-- Dependencies: 234
-- Data for Name: user_session_note; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3857 (class 0 OID 16503)
-- Dependencies: 216
-- Data for Name: username_login_failure; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--



--
-- TOC entry 3865 (class 0 OID 16552)
-- Dependencies: 224
-- Data for Name: web_origins; Type: TABLE DATA; Schema: public; Owner: keycloak_user
--

INSERT INTO public.web_origins (client_id, value) VALUES ('0e36bf7e-2a44-425f-a8d0-8206ad5f1e36', '+');
INSERT INTO public.web_origins (client_id, value) VALUES ('d6176e7d-5390-41da-b0ba-e8f4738ac7a1', '+');


--
-- TOC entry 3396 (class 2606 OID 17648)
-- Name: username_login_failure CONSTRAINT_17-2; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.username_login_failure
    ADD CONSTRAINT "CONSTRAINT_17-2" PRIMARY KEY (realm_id, username);


--
-- TOC entry 3369 (class 2606 OID 17962)
-- Name: keycloak_role UK_J3RWUVD56ONTGSUHOGM184WW2-2; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.keycloak_role
    ADD CONSTRAINT "UK_J3RWUVD56ONTGSUHOGM184WW2-2" UNIQUE (name, client_realm_constraint);


--
-- TOC entry 3610 (class 2606 OID 17787)
-- Name: client_auth_flow_bindings c_cli_flow_bind; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_auth_flow_bindings
    ADD CONSTRAINT c_cli_flow_bind PRIMARY KEY (client_id, binding_name);


--
-- TOC entry 3612 (class 2606 OID 17994)
-- Name: client_scope_client c_cli_scope_bind; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_scope_client
    ADD CONSTRAINT c_cli_scope_bind PRIMARY KEY (client_id, scope_id);


--
-- TOC entry 3607 (class 2606 OID 17662)
-- Name: client_initial_access cnstr_client_init_acc_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_initial_access
    ADD CONSTRAINT cnstr_client_init_acc_pk PRIMARY KEY (id);


--
-- TOC entry 3524 (class 2606 OID 17293)
-- Name: realm_default_groups con_group_id_def_groups; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_default_groups
    ADD CONSTRAINT con_group_id_def_groups UNIQUE (group_id);


--
-- TOC entry 3572 (class 2606 OID 17581)
-- Name: broker_link constr_broker_link_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.broker_link
    ADD CONSTRAINT constr_broker_link_pk PRIMARY KEY (identity_provider, user_id);


--
-- TOC entry 3494 (class 2606 OID 17198)
-- Name: client_user_session_note constr_cl_usr_ses_note; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_user_session_note
    ADD CONSTRAINT constr_cl_usr_ses_note PRIMARY KEY (client_session, name);


--
-- TOC entry 3598 (class 2606 OID 17601)
-- Name: component_config constr_component_config_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.component_config
    ADD CONSTRAINT constr_component_config_pk PRIMARY KEY (id);


--
-- TOC entry 3601 (class 2606 OID 17599)
-- Name: component constr_component_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.component
    ADD CONSTRAINT constr_component_pk PRIMARY KEY (id);


--
-- TOC entry 3590 (class 2606 OID 17597)
-- Name: fed_user_required_action constr_fed_required_action; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.fed_user_required_action
    ADD CONSTRAINT constr_fed_required_action PRIMARY KEY (required_action, user_id);


--
-- TOC entry 3574 (class 2606 OID 17583)
-- Name: fed_user_attribute constr_fed_user_attr_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.fed_user_attribute
    ADD CONSTRAINT constr_fed_user_attr_pk PRIMARY KEY (id);


--
-- TOC entry 3577 (class 2606 OID 17585)
-- Name: fed_user_consent constr_fed_user_consent_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.fed_user_consent
    ADD CONSTRAINT constr_fed_user_consent_pk PRIMARY KEY (id);


--
-- TOC entry 3582 (class 2606 OID 17591)
-- Name: fed_user_credential constr_fed_user_cred_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.fed_user_credential
    ADD CONSTRAINT constr_fed_user_cred_pk PRIMARY KEY (id);


--
-- TOC entry 3586 (class 2606 OID 17593)
-- Name: fed_user_group_membership constr_fed_user_group; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.fed_user_group_membership
    ADD CONSTRAINT constr_fed_user_group PRIMARY KEY (group_id, user_id);


--
-- TOC entry 3594 (class 2606 OID 17595)
-- Name: fed_user_role_mapping constr_fed_user_role; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.fed_user_role_mapping
    ADD CONSTRAINT constr_fed_user_role PRIMARY KEY (role_id, user_id);


--
-- TOC entry 3605 (class 2606 OID 17641)
-- Name: federated_user constr_federated_user; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.federated_user
    ADD CONSTRAINT constr_federated_user PRIMARY KEY (id);


--
-- TOC entry 3526 (class 2606 OID 17746)
-- Name: realm_default_groups constr_realm_default_groups; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_default_groups
    ADD CONSTRAINT constr_realm_default_groups PRIMARY KEY (realm_id, group_id);


--
-- TOC entry 3454 (class 2606 OID 17763)
-- Name: realm_enabled_event_types constr_realm_enabl_event_types; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_enabled_event_types
    ADD CONSTRAINT constr_realm_enabl_event_types PRIMARY KEY (realm_id, value);


--
-- TOC entry 3383 (class 2606 OID 17765)
-- Name: realm_events_listeners constr_realm_events_listeners; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_events_listeners
    ADD CONSTRAINT constr_realm_events_listeners PRIMARY KEY (realm_id, value);


--
-- TOC entry 3449 (class 2606 OID 17767)
-- Name: realm_supported_locales constr_realm_supported_locales; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_supported_locales
    ADD CONSTRAINT constr_realm_supported_locales PRIMARY KEY (realm_id, value);


--
-- TOC entry 3442 (class 2606 OID 16864)
-- Name: identity_provider constraint_2b; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.identity_provider
    ADD CONSTRAINT constraint_2b PRIMARY KEY (internal_id);


--
-- TOC entry 3425 (class 2606 OID 16792)
-- Name: client_attributes constraint_3c; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_attributes
    ADD CONSTRAINT constraint_3c PRIMARY KEY (client_id, name);


--
-- TOC entry 3366 (class 2606 OID 16564)
-- Name: event_entity constraint_4; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.event_entity
    ADD CONSTRAINT constraint_4 PRIMARY KEY (id);


--
-- TOC entry 3438 (class 2606 OID 16866)
-- Name: federated_identity constraint_40; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.federated_identity
    ADD CONSTRAINT constraint_40 PRIMARY KEY (identity_provider, user_id);


--
-- TOC entry 3375 (class 2606 OID 16566)
-- Name: realm constraint_4a; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm
    ADD CONSTRAINT constraint_4a PRIMARY KEY (id);


--
-- TOC entry 3357 (class 2606 OID 16568)
-- Name: client_session_role constraint_5; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_role
    ADD CONSTRAINT constraint_5 PRIMARY KEY (client_session, role_id);


--
-- TOC entry 3420 (class 2606 OID 16570)
-- Name: user_session constraint_57; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_session
    ADD CONSTRAINT constraint_57 PRIMARY KEY (id);


--
-- TOC entry 3411 (class 2606 OID 16572)
-- Name: user_federation_provider constraint_5c; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_provider
    ADD CONSTRAINT constraint_5c PRIMARY KEY (id);


--
-- TOC entry 3428 (class 2606 OID 16794)
-- Name: client_session_note constraint_5e; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_note
    ADD CONSTRAINT constraint_5e PRIMARY KEY (client_session, name);


--
-- TOC entry 3349 (class 2606 OID 16576)
-- Name: client constraint_7; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT constraint_7 PRIMARY KEY (id);


--
-- TOC entry 3354 (class 2606 OID 16578)
-- Name: client_session constraint_8; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session
    ADD CONSTRAINT constraint_8 PRIMARY KEY (id);


--
-- TOC entry 3393 (class 2606 OID 16580)
-- Name: scope_mapping constraint_81; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.scope_mapping
    ADD CONSTRAINT constraint_81 PRIMARY KEY (client_id, role_id);


--
-- TOC entry 3430 (class 2606 OID 16796)
-- Name: client_node_registrations constraint_84; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_node_registrations
    ADD CONSTRAINT constraint_84 PRIMARY KEY (client_id, name);


--
-- TOC entry 3380 (class 2606 OID 16582)
-- Name: realm_attribute constraint_9; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_attribute
    ADD CONSTRAINT constraint_9 PRIMARY KEY (name, realm_id);


--
-- TOC entry 3386 (class 2606 OID 16584)
-- Name: realm_required_credential constraint_92; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_required_credential
    ADD CONSTRAINT constraint_92 PRIMARY KEY (realm_id, type);


--
-- TOC entry 3371 (class 2606 OID 16586)
-- Name: keycloak_role constraint_a; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.keycloak_role
    ADD CONSTRAINT constraint_a PRIMARY KEY (id);


--
-- TOC entry 3472 (class 2606 OID 17750)
-- Name: admin_event_entity constraint_admin_event_entity; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.admin_event_entity
    ADD CONSTRAINT constraint_admin_event_entity PRIMARY KEY (id);


--
-- TOC entry 3484 (class 2606 OID 17103)
-- Name: authenticator_config_entry constraint_auth_cfg_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authenticator_config_entry
    ADD CONSTRAINT constraint_auth_cfg_pk PRIMARY KEY (authenticator_id, name);


--
-- TOC entry 3480 (class 2606 OID 17101)
-- Name: authentication_execution constraint_auth_exec_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authentication_execution
    ADD CONSTRAINT constraint_auth_exec_pk PRIMARY KEY (id);


--
-- TOC entry 3477 (class 2606 OID 17099)
-- Name: authentication_flow constraint_auth_flow_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authentication_flow
    ADD CONSTRAINT constraint_auth_flow_pk PRIMARY KEY (id);


--
-- TOC entry 3474 (class 2606 OID 17097)
-- Name: authenticator_config constraint_auth_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authenticator_config
    ADD CONSTRAINT constraint_auth_pk PRIMARY KEY (id);


--
-- TOC entry 3492 (class 2606 OID 17107)
-- Name: client_session_auth_status constraint_auth_status_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_auth_status
    ADD CONSTRAINT constraint_auth_status_pk PRIMARY KEY (client_session, authenticator);


--
-- TOC entry 3417 (class 2606 OID 16588)
-- Name: user_role_mapping constraint_c; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_role_mapping
    ADD CONSTRAINT constraint_c PRIMARY KEY (role_id, user_id);


--
-- TOC entry 3359 (class 2606 OID 17744)
-- Name: composite_role constraint_composite_role; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.composite_role
    ADD CONSTRAINT constraint_composite_role PRIMARY KEY (composite, child_role);


--
-- TOC entry 3470 (class 2606 OID 16984)
-- Name: client_session_prot_mapper constraint_cs_pmp_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_prot_mapper
    ADD CONSTRAINT constraint_cs_pmp_pk PRIMARY KEY (client_session, protocol_mapper_id);


--
-- TOC entry 3447 (class 2606 OID 16868)
-- Name: identity_provider_config constraint_d; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.identity_provider_config
    ADD CONSTRAINT constraint_d PRIMARY KEY (identity_provider_id, name);


--
-- TOC entry 3558 (class 2606 OID 17458)
-- Name: policy_config constraint_dpc; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.policy_config
    ADD CONSTRAINT constraint_dpc PRIMARY KEY (policy_id, name);


--
-- TOC entry 3388 (class 2606 OID 16590)
-- Name: realm_smtp_config constraint_e; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_smtp_config
    ADD CONSTRAINT constraint_e PRIMARY KEY (realm_id, name);


--
-- TOC entry 3363 (class 2606 OID 16592)
-- Name: credential constraint_f; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.credential
    ADD CONSTRAINT constraint_f PRIMARY KEY (id);


--
-- TOC entry 3409 (class 2606 OID 16594)
-- Name: user_federation_config constraint_f9; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_config
    ADD CONSTRAINT constraint_f9 PRIMARY KEY (user_federation_provider_id, name);


--
-- TOC entry 3625 (class 2606 OID 17886)
-- Name: resource_server_perm_ticket constraint_fapmt; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT constraint_fapmt PRIMARY KEY (id);


--
-- TOC entry 3543 (class 2606 OID 17413)
-- Name: resource_server_resource constraint_farsr; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_resource
    ADD CONSTRAINT constraint_farsr PRIMARY KEY (id);


--
-- TOC entry 3553 (class 2606 OID 17443)
-- Name: resource_server_policy constraint_farsrp; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_policy
    ADD CONSTRAINT constraint_farsrp PRIMARY KEY (id);


--
-- TOC entry 3569 (class 2606 OID 17513)
-- Name: associated_policy constraint_farsrpap; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.associated_policy
    ADD CONSTRAINT constraint_farsrpap PRIMARY KEY (policy_id, associated_policy_id);


--
-- TOC entry 3563 (class 2606 OID 17483)
-- Name: resource_policy constraint_farsrpp; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_policy
    ADD CONSTRAINT constraint_farsrpp PRIMARY KEY (resource_id, policy_id);


--
-- TOC entry 3548 (class 2606 OID 17428)
-- Name: resource_server_scope constraint_farsrs; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_scope
    ADD CONSTRAINT constraint_farsrs PRIMARY KEY (id);


--
-- TOC entry 3560 (class 2606 OID 17468)
-- Name: resource_scope constraint_farsrsp; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_scope
    ADD CONSTRAINT constraint_farsrsp PRIMARY KEY (resource_id, scope_id);


--
-- TOC entry 3566 (class 2606 OID 17498)
-- Name: scope_policy constraint_farsrsps; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.scope_policy
    ADD CONSTRAINT constraint_farsrsps PRIMARY KEY (scope_id, policy_id);


--
-- TOC entry 3402 (class 2606 OID 16596)
-- Name: user_entity constraint_fb; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT constraint_fb PRIMARY KEY (id);


--
-- TOC entry 3490 (class 2606 OID 17111)
-- Name: user_federation_mapper_config constraint_fedmapper_cfg_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_mapper_config
    ADD CONSTRAINT constraint_fedmapper_cfg_pm PRIMARY KEY (user_federation_mapper_id, name);


--
-- TOC entry 3486 (class 2606 OID 17109)
-- Name: user_federation_mapper constraint_fedmapperpm; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_mapper
    ADD CONSTRAINT constraint_fedmapperpm PRIMARY KEY (id);


--
-- TOC entry 3623 (class 2606 OID 17871)
-- Name: fed_user_consent_cl_scope constraint_fgrntcsnt_clsc_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.fed_user_consent_cl_scope
    ADD CONSTRAINT constraint_fgrntcsnt_clsc_pm PRIMARY KEY (user_consent_id, scope_id);


--
-- TOC entry 3620 (class 2606 OID 17861)
-- Name: user_consent_client_scope constraint_grntcsnt_clsc_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_consent_client_scope
    ADD CONSTRAINT constraint_grntcsnt_clsc_pm PRIMARY KEY (user_consent_id, scope_id);


--
-- TOC entry 3465 (class 2606 OID 16978)
-- Name: user_consent constraint_grntcsnt_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_consent
    ADD CONSTRAINT constraint_grntcsnt_pm PRIMARY KEY (id);


--
-- TOC entry 3511 (class 2606 OID 17260)
-- Name: keycloak_group constraint_group; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.keycloak_group
    ADD CONSTRAINT constraint_group PRIMARY KEY (id);


--
-- TOC entry 3518 (class 2606 OID 17267)
-- Name: group_attribute constraint_group_attribute_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.group_attribute
    ADD CONSTRAINT constraint_group_attribute_pk PRIMARY KEY (id);


--
-- TOC entry 3515 (class 2606 OID 17281)
-- Name: group_role_mapping constraint_group_role; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.group_role_mapping
    ADD CONSTRAINT constraint_group_role PRIMARY KEY (role_id, group_id);


--
-- TOC entry 3460 (class 2606 OID 16974)
-- Name: identity_provider_mapper constraint_idpm; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.identity_provider_mapper
    ADD CONSTRAINT constraint_idpm PRIMARY KEY (id);


--
-- TOC entry 3463 (class 2606 OID 17160)
-- Name: idp_mapper_config constraint_idpmconfig; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.idp_mapper_config
    ADD CONSTRAINT constraint_idpmconfig PRIMARY KEY (idp_mapper_id, name);


--
-- TOC entry 3457 (class 2606 OID 16972)
-- Name: migration_model constraint_migmod; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.migration_model
    ADD CONSTRAINT constraint_migmod PRIMARY KEY (id);


--
-- TOC entry 3507 (class 2606 OID 17969)
-- Name: offline_client_session constraint_offl_cl_ses_pk3; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.offline_client_session
    ADD CONSTRAINT constraint_offl_cl_ses_pk3 PRIMARY KEY (user_session_id, client_id, client_storage_provider, external_client_id, offline_flag);


--
-- TOC entry 3501 (class 2606 OID 17235)
-- Name: offline_user_session constraint_offl_us_ses_pk2; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.offline_user_session
    ADD CONSTRAINT constraint_offl_us_ses_pk2 PRIMARY KEY (user_session_id, offline_flag);


--
-- TOC entry 3432 (class 2606 OID 16862)
-- Name: protocol_mapper constraint_pcm; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.protocol_mapper
    ADD CONSTRAINT constraint_pcm PRIMARY KEY (id);


--
-- TOC entry 3436 (class 2606 OID 17153)
-- Name: protocol_mapper_config constraint_pmconfig; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.protocol_mapper_config
    ADD CONSTRAINT constraint_pmconfig PRIMARY KEY (protocol_mapper_id, name);


--
-- TOC entry 3390 (class 2606 OID 17769)
-- Name: redirect_uris constraint_redirect_uris; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.redirect_uris
    ADD CONSTRAINT constraint_redirect_uris PRIMARY KEY (client_id, value);


--
-- TOC entry 3499 (class 2606 OID 17196)
-- Name: required_action_config constraint_req_act_cfg_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.required_action_config
    ADD CONSTRAINT constraint_req_act_cfg_pk PRIMARY KEY (required_action_id, name);


--
-- TOC entry 3496 (class 2606 OID 17194)
-- Name: required_action_provider constraint_req_act_prv_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.required_action_provider
    ADD CONSTRAINT constraint_req_act_prv_pk PRIMARY KEY (id);


--
-- TOC entry 3414 (class 2606 OID 17105)
-- Name: user_required_action constraint_required_action; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_required_action
    ADD CONSTRAINT constraint_required_action PRIMARY KEY (required_action, user_id);


--
-- TOC entry 3631 (class 2606 OID 17934)
-- Name: resource_uris constraint_resour_uris_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_uris
    ADD CONSTRAINT constraint_resour_uris_pk PRIMARY KEY (resource_id, value);


--
-- TOC entry 3633 (class 2606 OID 17942)
-- Name: role_attribute constraint_role_attribute_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.role_attribute
    ADD CONSTRAINT constraint_role_attribute_pk PRIMARY KEY (id);


--
-- TOC entry 3398 (class 2606 OID 17192)
-- Name: user_attribute constraint_user_attribute_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_attribute
    ADD CONSTRAINT constraint_user_attribute_pk PRIMARY KEY (id);


--
-- TOC entry 3521 (class 2606 OID 17274)
-- Name: user_group_membership constraint_user_group; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_group_membership
    ADD CONSTRAINT constraint_user_group PRIMARY KEY (group_id, user_id);


--
-- TOC entry 3452 (class 2606 OID 16872)
-- Name: user_session_note constraint_usn_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_session_note
    ADD CONSTRAINT constraint_usn_pk PRIMARY KEY (user_session, name);


--
-- TOC entry 3422 (class 2606 OID 17771)
-- Name: web_origins constraint_web_origins; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.web_origins
    ADD CONSTRAINT constraint_web_origins PRIMARY KEY (client_id, value);


--
-- TOC entry 3535 (class 2606 OID 17379)
-- Name: client_scope_attributes pk_cl_tmpl_attr; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_scope_attributes
    ADD CONSTRAINT pk_cl_tmpl_attr PRIMARY KEY (scope_id, name);


--
-- TOC entry 3530 (class 2606 OID 17338)
-- Name: client_scope pk_cli_template; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_scope
    ADD CONSTRAINT pk_cli_template PRIMARY KEY (id);


--
-- TOC entry 3347 (class 2606 OID 16391)
-- Name: databasechangeloglock pk_databasechangeloglock; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- TOC entry 3541 (class 2606 OID 17724)
-- Name: resource_server pk_resource_server; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server
    ADD CONSTRAINT pk_resource_server PRIMARY KEY (id);


--
-- TOC entry 3539 (class 2606 OID 17367)
-- Name: client_scope_role_mapping pk_template_scope; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_scope_role_mapping
    ADD CONSTRAINT pk_template_scope PRIMARY KEY (scope_id, role_id);


--
-- TOC entry 3618 (class 2606 OID 17846)
-- Name: default_client_scope r_def_cli_scope_bind; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.default_client_scope
    ADD CONSTRAINT r_def_cli_scope_bind PRIMARY KEY (realm_id, scope_id);


--
-- TOC entry 3636 (class 2606 OID 17988)
-- Name: realm_localizations realm_localizations_pkey; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_localizations
    ADD CONSTRAINT realm_localizations_pkey PRIMARY KEY (realm_id, locale);


--
-- TOC entry 3629 (class 2606 OID 17914)
-- Name: resource_attribute res_attr_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_attribute
    ADD CONSTRAINT res_attr_pk PRIMARY KEY (id);


--
-- TOC entry 3513 (class 2606 OID 17654)
-- Name: keycloak_group sibling_names; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.keycloak_group
    ADD CONSTRAINT sibling_names UNIQUE (realm_id, parent_group, name);


--
-- TOC entry 3445 (class 2606 OID 16919)
-- Name: identity_provider uk_2daelwnibji49avxsrtuf6xj33; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.identity_provider
    ADD CONSTRAINT uk_2daelwnibji49avxsrtuf6xj33 UNIQUE (provider_alias, realm_id);


--
-- TOC entry 3352 (class 2606 OID 16600)
-- Name: client uk_b71cjlbenv945rb6gcon438at; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT uk_b71cjlbenv945rb6gcon438at UNIQUE (realm_id, client_id);


--
-- TOC entry 3532 (class 2606 OID 17799)
-- Name: client_scope uk_cli_scope; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_scope
    ADD CONSTRAINT uk_cli_scope UNIQUE (realm_id, name);


--
-- TOC entry 3405 (class 2606 OID 16604)
-- Name: user_entity uk_dykn684sl8up1crfei6eckhd7; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT uk_dykn684sl8up1crfei6eckhd7 UNIQUE (realm_id, email_constraint);


--
-- TOC entry 3546 (class 2606 OID 17978)
-- Name: resource_server_resource uk_frsr6t700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_resource
    ADD CONSTRAINT uk_frsr6t700s9v50bu18ws5ha6 UNIQUE (name, owner, resource_server_id);


--
-- TOC entry 3627 (class 2606 OID 17973)
-- Name: resource_server_perm_ticket uk_frsr6t700s9v50bu18ws5pmt; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT uk_frsr6t700s9v50bu18ws5pmt UNIQUE (owner, requester, resource_server_id, resource_id, scope_id);


--
-- TOC entry 3556 (class 2606 OID 17715)
-- Name: resource_server_policy uk_frsrpt700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_policy
    ADD CONSTRAINT uk_frsrpt700s9v50bu18ws5ha6 UNIQUE (name, resource_server_id);


--
-- TOC entry 3551 (class 2606 OID 17719)
-- Name: resource_server_scope uk_frsrst700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_scope
    ADD CONSTRAINT uk_frsrst700s9v50bu18ws5ha6 UNIQUE (name, resource_server_id);


--
-- TOC entry 3468 (class 2606 OID 17964)
-- Name: user_consent uk_jkuwuvd56ontgsuhogm8uewrt; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_consent
    ADD CONSTRAINT uk_jkuwuvd56ontgsuhogm8uewrt UNIQUE (client_id, client_storage_provider, external_client_id, user_id);


--
-- TOC entry 3378 (class 2606 OID 16612)
-- Name: realm uk_orvsdmla56612eaefiq6wl5oi; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm
    ADD CONSTRAINT uk_orvsdmla56612eaefiq6wl5oi UNIQUE (name);


--
-- TOC entry 3407 (class 2606 OID 17643)
-- Name: user_entity uk_ru8tt6t700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT uk_ru8tt6t700s9v50bu18ws5ha6 UNIQUE (realm_id, username);


--
-- TOC entry 3570 (class 1259 OID 17668)
-- Name: idx_assoc_pol_assoc_pol_id; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_assoc_pol_assoc_pol_id ON public.associated_policy USING btree (associated_policy_id);


--
-- TOC entry 3475 (class 1259 OID 17672)
-- Name: idx_auth_config_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_auth_config_realm ON public.authenticator_config USING btree (realm_id);


--
-- TOC entry 3481 (class 1259 OID 17670)
-- Name: idx_auth_exec_flow; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_auth_exec_flow ON public.authentication_execution USING btree (flow_id);


--
-- TOC entry 3482 (class 1259 OID 17669)
-- Name: idx_auth_exec_realm_flow; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_auth_exec_realm_flow ON public.authentication_execution USING btree (realm_id, flow_id);


--
-- TOC entry 3478 (class 1259 OID 17671)
-- Name: idx_auth_flow_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_auth_flow_realm ON public.authentication_flow USING btree (realm_id);


--
-- TOC entry 3613 (class 1259 OID 17995)
-- Name: idx_cl_clscope; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_cl_clscope ON public.client_scope_client USING btree (scope_id);


--
-- TOC entry 3426 (class 1259 OID 18002)
-- Name: idx_client_att_by_name_value; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_client_att_by_name_value ON public.client_attributes USING btree (name, ((value)::character varying(250)));


--
-- TOC entry 3350 (class 1259 OID 17979)
-- Name: idx_client_id; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_client_id ON public.client USING btree (client_id);


--
-- TOC entry 3608 (class 1259 OID 17712)
-- Name: idx_client_init_acc_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_client_init_acc_realm ON public.client_initial_access USING btree (realm_id);


--
-- TOC entry 3355 (class 1259 OID 17676)
-- Name: idx_client_session_session; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_client_session_session ON public.client_session USING btree (session_id);


--
-- TOC entry 3533 (class 1259 OID 17876)
-- Name: idx_clscope_attrs; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_clscope_attrs ON public.client_scope_attributes USING btree (scope_id);


--
-- TOC entry 3614 (class 1259 OID 17992)
-- Name: idx_clscope_cl; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_clscope_cl ON public.client_scope_client USING btree (client_id);


--
-- TOC entry 3433 (class 1259 OID 17873)
-- Name: idx_clscope_protmap; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_clscope_protmap ON public.protocol_mapper USING btree (client_scope_id);


--
-- TOC entry 3536 (class 1259 OID 17874)
-- Name: idx_clscope_role; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_clscope_role ON public.client_scope_role_mapping USING btree (scope_id);


--
-- TOC entry 3599 (class 1259 OID 17678)
-- Name: idx_compo_config_compo; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_compo_config_compo ON public.component_config USING btree (component_id);


--
-- TOC entry 3602 (class 1259 OID 17949)
-- Name: idx_component_provider_type; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_component_provider_type ON public.component USING btree (provider_type);


--
-- TOC entry 3603 (class 1259 OID 17677)
-- Name: idx_component_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_component_realm ON public.component USING btree (realm_id);


--
-- TOC entry 3360 (class 1259 OID 17679)
-- Name: idx_composite; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_composite ON public.composite_role USING btree (composite);


--
-- TOC entry 3361 (class 1259 OID 17680)
-- Name: idx_composite_child; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_composite_child ON public.composite_role USING btree (child_role);


--
-- TOC entry 3615 (class 1259 OID 17879)
-- Name: idx_defcls_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_defcls_realm ON public.default_client_scope USING btree (realm_id);


--
-- TOC entry 3616 (class 1259 OID 17880)
-- Name: idx_defcls_scope; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_defcls_scope ON public.default_client_scope USING btree (scope_id);


--
-- TOC entry 3367 (class 1259 OID 17980)
-- Name: idx_event_time; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_event_time ON public.event_entity USING btree (realm_id, event_time);


--
-- TOC entry 3439 (class 1259 OID 17397)
-- Name: idx_fedidentity_feduser; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fedidentity_feduser ON public.federated_identity USING btree (federated_user_id);


--
-- TOC entry 3440 (class 1259 OID 17396)
-- Name: idx_fedidentity_user; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fedidentity_user ON public.federated_identity USING btree (user_id);


--
-- TOC entry 3575 (class 1259 OID 17772)
-- Name: idx_fu_attribute; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_attribute ON public.fed_user_attribute USING btree (user_id, realm_id, name);


--
-- TOC entry 3578 (class 1259 OID 17793)
-- Name: idx_fu_cnsnt_ext; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_cnsnt_ext ON public.fed_user_consent USING btree (user_id, client_storage_provider, external_client_id);


--
-- TOC entry 3579 (class 1259 OID 17960)
-- Name: idx_fu_consent; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_consent ON public.fed_user_consent USING btree (user_id, client_id);


--
-- TOC entry 3580 (class 1259 OID 17774)
-- Name: idx_fu_consent_ru; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_consent_ru ON public.fed_user_consent USING btree (realm_id, user_id);


--
-- TOC entry 3583 (class 1259 OID 17775)
-- Name: idx_fu_credential; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_credential ON public.fed_user_credential USING btree (user_id, type);


--
-- TOC entry 3584 (class 1259 OID 17776)
-- Name: idx_fu_credential_ru; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_credential_ru ON public.fed_user_credential USING btree (realm_id, user_id);


--
-- TOC entry 3587 (class 1259 OID 17777)
-- Name: idx_fu_group_membership; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_group_membership ON public.fed_user_group_membership USING btree (user_id, group_id);


--
-- TOC entry 3588 (class 1259 OID 17778)
-- Name: idx_fu_group_membership_ru; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_group_membership_ru ON public.fed_user_group_membership USING btree (realm_id, user_id);


--
-- TOC entry 3591 (class 1259 OID 17779)
-- Name: idx_fu_required_action; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_required_action ON public.fed_user_required_action USING btree (user_id, required_action);


--
-- TOC entry 3592 (class 1259 OID 17780)
-- Name: idx_fu_required_action_ru; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_required_action_ru ON public.fed_user_required_action USING btree (realm_id, user_id);


--
-- TOC entry 3595 (class 1259 OID 17781)
-- Name: idx_fu_role_mapping; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_role_mapping ON public.fed_user_role_mapping USING btree (user_id, role_id);


--
-- TOC entry 3596 (class 1259 OID 17782)
-- Name: idx_fu_role_mapping_ru; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_fu_role_mapping_ru ON public.fed_user_role_mapping USING btree (realm_id, user_id);


--
-- TOC entry 3519 (class 1259 OID 17683)
-- Name: idx_group_attr_group; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_group_attr_group ON public.group_attribute USING btree (group_id);


--
-- TOC entry 3516 (class 1259 OID 17684)
-- Name: idx_group_role_mapp_group; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_group_role_mapp_group ON public.group_role_mapping USING btree (group_id);


--
-- TOC entry 3461 (class 1259 OID 17686)
-- Name: idx_id_prov_mapp_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_id_prov_mapp_realm ON public.identity_provider_mapper USING btree (realm_id);


--
-- TOC entry 3443 (class 1259 OID 17685)
-- Name: idx_ident_prov_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_ident_prov_realm ON public.identity_provider USING btree (realm_id);


--
-- TOC entry 3372 (class 1259 OID 17687)
-- Name: idx_keycloak_role_client; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_keycloak_role_client ON public.keycloak_role USING btree (client);


--
-- TOC entry 3373 (class 1259 OID 17688)
-- Name: idx_keycloak_role_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_keycloak_role_realm ON public.keycloak_role USING btree (realm);


--
-- TOC entry 3508 (class 1259 OID 17999)
-- Name: idx_offline_css_preload; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_offline_css_preload ON public.offline_client_session USING btree (client_id, offline_flag);


--
-- TOC entry 3502 (class 1259 OID 18000)
-- Name: idx_offline_uss_by_user; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_offline_uss_by_user ON public.offline_user_session USING btree (user_id, realm_id, offline_flag);


--
-- TOC entry 3503 (class 1259 OID 18001)
-- Name: idx_offline_uss_by_usersess; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_offline_uss_by_usersess ON public.offline_user_session USING btree (realm_id, offline_flag, user_session_id);


--
-- TOC entry 3504 (class 1259 OID 17953)
-- Name: idx_offline_uss_createdon; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_offline_uss_createdon ON public.offline_user_session USING btree (created_on);


--
-- TOC entry 3505 (class 1259 OID 17989)
-- Name: idx_offline_uss_preload; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_offline_uss_preload ON public.offline_user_session USING btree (offline_flag, created_on, user_session_id);


--
-- TOC entry 3434 (class 1259 OID 17689)
-- Name: idx_protocol_mapper_client; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_protocol_mapper_client ON public.protocol_mapper USING btree (client_id);


--
-- TOC entry 3381 (class 1259 OID 17692)
-- Name: idx_realm_attr_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_realm_attr_realm ON public.realm_attribute USING btree (realm_id);


--
-- TOC entry 3528 (class 1259 OID 17872)
-- Name: idx_realm_clscope; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_realm_clscope ON public.client_scope USING btree (realm_id);


--
-- TOC entry 3527 (class 1259 OID 17693)
-- Name: idx_realm_def_grp_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_realm_def_grp_realm ON public.realm_default_groups USING btree (realm_id);


--
-- TOC entry 3384 (class 1259 OID 17696)
-- Name: idx_realm_evt_list_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_realm_evt_list_realm ON public.realm_events_listeners USING btree (realm_id);


--
-- TOC entry 3455 (class 1259 OID 17695)
-- Name: idx_realm_evt_types_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_realm_evt_types_realm ON public.realm_enabled_event_types USING btree (realm_id);


--
-- TOC entry 3376 (class 1259 OID 17691)
-- Name: idx_realm_master_adm_cli; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_realm_master_adm_cli ON public.realm USING btree (master_admin_client);


--
-- TOC entry 3450 (class 1259 OID 17697)
-- Name: idx_realm_supp_local_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_realm_supp_local_realm ON public.realm_supported_locales USING btree (realm_id);


--
-- TOC entry 3391 (class 1259 OID 17698)
-- Name: idx_redir_uri_client; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_redir_uri_client ON public.redirect_uris USING btree (client_id);


--
-- TOC entry 3497 (class 1259 OID 17699)
-- Name: idx_req_act_prov_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_req_act_prov_realm ON public.required_action_provider USING btree (realm_id);


--
-- TOC entry 3564 (class 1259 OID 17700)
-- Name: idx_res_policy_policy; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_res_policy_policy ON public.resource_policy USING btree (policy_id);


--
-- TOC entry 3561 (class 1259 OID 17701)
-- Name: idx_res_scope_scope; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_res_scope_scope ON public.resource_scope USING btree (scope_id);


--
-- TOC entry 3554 (class 1259 OID 17720)
-- Name: idx_res_serv_pol_res_serv; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_res_serv_pol_res_serv ON public.resource_server_policy USING btree (resource_server_id);


--
-- TOC entry 3544 (class 1259 OID 17721)
-- Name: idx_res_srv_res_res_srv; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_res_srv_res_res_srv ON public.resource_server_resource USING btree (resource_server_id);


--
-- TOC entry 3549 (class 1259 OID 17722)
-- Name: idx_res_srv_scope_res_srv; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_res_srv_scope_res_srv ON public.resource_server_scope USING btree (resource_server_id);


--
-- TOC entry 3634 (class 1259 OID 17948)
-- Name: idx_role_attribute; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_role_attribute ON public.role_attribute USING btree (role_id);


--
-- TOC entry 3537 (class 1259 OID 17875)
-- Name: idx_role_clscope; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_role_clscope ON public.client_scope_role_mapping USING btree (role_id);


--
-- TOC entry 3394 (class 1259 OID 17705)
-- Name: idx_scope_mapping_role; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_scope_mapping_role ON public.scope_mapping USING btree (role_id);


--
-- TOC entry 3567 (class 1259 OID 17706)
-- Name: idx_scope_policy_policy; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_scope_policy_policy ON public.scope_policy USING btree (policy_id);


--
-- TOC entry 3458 (class 1259 OID 17958)
-- Name: idx_update_time; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_update_time ON public.migration_model USING btree (update_time);


--
-- TOC entry 3509 (class 1259 OID 17386)
-- Name: idx_us_sess_id_on_cl_sess; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_us_sess_id_on_cl_sess ON public.offline_client_session USING btree (user_session_id);


--
-- TOC entry 3621 (class 1259 OID 17881)
-- Name: idx_usconsent_clscope; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_usconsent_clscope ON public.user_consent_client_scope USING btree (user_consent_id);


--
-- TOC entry 3399 (class 1259 OID 17393)
-- Name: idx_user_attribute; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_attribute ON public.user_attribute USING btree (user_id);


--
-- TOC entry 3400 (class 1259 OID 18003)
-- Name: idx_user_attribute_name; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_attribute_name ON public.user_attribute USING btree (name, value);


--
-- TOC entry 3466 (class 1259 OID 17390)
-- Name: idx_user_consent; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_consent ON public.user_consent USING btree (user_id);


--
-- TOC entry 3364 (class 1259 OID 17394)
-- Name: idx_user_credential; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_credential ON public.credential USING btree (user_id);


--
-- TOC entry 3403 (class 1259 OID 17387)
-- Name: idx_user_email; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_email ON public.user_entity USING btree (email);


--
-- TOC entry 3522 (class 1259 OID 17389)
-- Name: idx_user_group_mapping; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_group_mapping ON public.user_group_membership USING btree (user_id);


--
-- TOC entry 3415 (class 1259 OID 17395)
-- Name: idx_user_reqactions; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_reqactions ON public.user_required_action USING btree (user_id);


--
-- TOC entry 3418 (class 1259 OID 17388)
-- Name: idx_user_role_mapping; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_user_role_mapping ON public.user_role_mapping USING btree (user_id);


--
-- TOC entry 3487 (class 1259 OID 17708)
-- Name: idx_usr_fed_map_fed_prv; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_usr_fed_map_fed_prv ON public.user_federation_mapper USING btree (federation_provider_id);


--
-- TOC entry 3488 (class 1259 OID 17709)
-- Name: idx_usr_fed_map_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_usr_fed_map_realm ON public.user_federation_mapper USING btree (realm_id);


--
-- TOC entry 3412 (class 1259 OID 17710)
-- Name: idx_usr_fed_prv_realm; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_usr_fed_prv_realm ON public.user_federation_provider USING btree (realm_id);


--
-- TOC entry 3423 (class 1259 OID 17711)
-- Name: idx_web_orig_client; Type: INDEX; Schema: public; Owner: keycloak_user
--

CREATE INDEX idx_web_orig_client ON public.web_origins USING btree (client_id);


--
-- TOC entry 3678 (class 2606 OID 17112)
-- Name: client_session_auth_status auth_status_constraint; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_auth_status
    ADD CONSTRAINT auth_status_constraint FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- TOC entry 3662 (class 2606 OID 16873)
-- Name: identity_provider fk2b4ebc52ae5c3b34; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.identity_provider
    ADD CONSTRAINT fk2b4ebc52ae5c3b34 FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3655 (class 2606 OID 16797)
-- Name: client_attributes fk3c47c64beacca966; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_attributes
    ADD CONSTRAINT fk3c47c64beacca966 FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- TOC entry 3661 (class 2606 OID 16883)
-- Name: federated_identity fk404288b92ef007a6; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.federated_identity
    ADD CONSTRAINT fk404288b92ef007a6 FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 3657 (class 2606 OID 17032)
-- Name: client_node_registrations fk4129723ba992f594; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_node_registrations
    ADD CONSTRAINT fk4129723ba992f594 FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- TOC entry 3656 (class 2606 OID 16802)
-- Name: client_session_note fk5edfb00ff51c2736; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_note
    ADD CONSTRAINT fk5edfb00ff51c2736 FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- TOC entry 3665 (class 2606 OID 16913)
-- Name: user_session_note fk5edfb00ff51d3472; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_session_note
    ADD CONSTRAINT fk5edfb00ff51d3472 FOREIGN KEY (user_session) REFERENCES public.user_session(id);


--
-- TOC entry 3638 (class 2606 OID 16615)
-- Name: client_session_role fk_11b7sgqw18i532811v7o2dv76; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_role
    ADD CONSTRAINT fk_11b7sgqw18i532811v7o2dv76 FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- TOC entry 3647 (class 2606 OID 16620)
-- Name: redirect_uris fk_1burs8pb4ouj97h5wuppahv9f; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.redirect_uris
    ADD CONSTRAINT fk_1burs8pb4ouj97h5wuppahv9f FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- TOC entry 3651 (class 2606 OID 16625)
-- Name: user_federation_provider fk_1fj32f6ptolw2qy60cd8n01e8; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_provider
    ADD CONSTRAINT fk_1fj32f6ptolw2qy60cd8n01e8 FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3670 (class 2606 OID 17010)
-- Name: client_session_prot_mapper fk_33a8sgqw18i532811v7o2dk89; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session_prot_mapper
    ADD CONSTRAINT fk_33a8sgqw18i532811v7o2dk89 FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- TOC entry 3645 (class 2606 OID 16635)
-- Name: realm_required_credential fk_5hg65lybevavkqfki3kponh9v; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_required_credential
    ADD CONSTRAINT fk_5hg65lybevavkqfki3kponh9v FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3708 (class 2606 OID 17915)
-- Name: resource_attribute fk_5hrm2vlf9ql5fu022kqepovbr; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_attribute
    ADD CONSTRAINT fk_5hrm2vlf9ql5fu022kqepovbr FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- TOC entry 3649 (class 2606 OID 16640)
-- Name: user_attribute fk_5hrm2vlf9ql5fu043kqepovbr; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_attribute
    ADD CONSTRAINT fk_5hrm2vlf9ql5fu043kqepovbr FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 3652 (class 2606 OID 16650)
-- Name: user_required_action fk_6qj3w1jw9cvafhe19bwsiuvmd; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_required_action
    ADD CONSTRAINT fk_6qj3w1jw9cvafhe19bwsiuvmd FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 3642 (class 2606 OID 16655)
-- Name: keycloak_role fk_6vyqfe4cn4wlq8r6kt5vdsj5c; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.keycloak_role
    ADD CONSTRAINT fk_6vyqfe4cn4wlq8r6kt5vdsj5c FOREIGN KEY (realm) REFERENCES public.realm(id);


--
-- TOC entry 3646 (class 2606 OID 16660)
-- Name: realm_smtp_config fk_70ej8xdxgxd0b9hh6180irr0o; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_smtp_config
    ADD CONSTRAINT fk_70ej8xdxgxd0b9hh6180irr0o FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3643 (class 2606 OID 16675)
-- Name: realm_attribute fk_8shxd6l3e9atqukacxgpffptw; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_attribute
    ADD CONSTRAINT fk_8shxd6l3e9atqukacxgpffptw FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3639 (class 2606 OID 16680)
-- Name: composite_role fk_a63wvekftu8jo1pnj81e7mce2; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.composite_role
    ADD CONSTRAINT fk_a63wvekftu8jo1pnj81e7mce2 FOREIGN KEY (composite) REFERENCES public.keycloak_role(id);


--
-- TOC entry 3674 (class 2606 OID 17132)
-- Name: authentication_execution fk_auth_exec_flow; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authentication_execution
    ADD CONSTRAINT fk_auth_exec_flow FOREIGN KEY (flow_id) REFERENCES public.authentication_flow(id);


--
-- TOC entry 3673 (class 2606 OID 17127)
-- Name: authentication_execution fk_auth_exec_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authentication_execution
    ADD CONSTRAINT fk_auth_exec_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3672 (class 2606 OID 17122)
-- Name: authentication_flow fk_auth_flow_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authentication_flow
    ADD CONSTRAINT fk_auth_flow_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3671 (class 2606 OID 17117)
-- Name: authenticator_config fk_auth_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.authenticator_config
    ADD CONSTRAINT fk_auth_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3637 (class 2606 OID 16685)
-- Name: client_session fk_b4ao2vcvat6ukau74wbwtfqo1; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_session
    ADD CONSTRAINT fk_b4ao2vcvat6ukau74wbwtfqo1 FOREIGN KEY (session_id) REFERENCES public.user_session(id);


--
-- TOC entry 3653 (class 2606 OID 16690)
-- Name: user_role_mapping fk_c4fqv34p1mbylloxang7b1q3l; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_role_mapping
    ADD CONSTRAINT fk_c4fqv34p1mbylloxang7b1q3l FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 3685 (class 2606 OID 17820)
-- Name: client_scope_attributes fk_cl_scope_attr_scope; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_scope_attributes
    ADD CONSTRAINT fk_cl_scope_attr_scope FOREIGN KEY (scope_id) REFERENCES public.client_scope(id);


--
-- TOC entry 3686 (class 2606 OID 17810)
-- Name: client_scope_role_mapping fk_cl_scope_rm_scope; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_scope_role_mapping
    ADD CONSTRAINT fk_cl_scope_rm_scope FOREIGN KEY (scope_id) REFERENCES public.client_scope(id);


--
-- TOC entry 3679 (class 2606 OID 17204)
-- Name: client_user_session_note fk_cl_usr_ses_note; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_user_session_note
    ADD CONSTRAINT fk_cl_usr_ses_note FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- TOC entry 3659 (class 2606 OID 17805)
-- Name: protocol_mapper fk_cli_scope_mapper; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.protocol_mapper
    ADD CONSTRAINT fk_cli_scope_mapper FOREIGN KEY (client_scope_id) REFERENCES public.client_scope(id);


--
-- TOC entry 3701 (class 2606 OID 17663)
-- Name: client_initial_access fk_client_init_acc_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.client_initial_access
    ADD CONSTRAINT fk_client_init_acc_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3699 (class 2606 OID 17607)
-- Name: component_config fk_component_config; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.component_config
    ADD CONSTRAINT fk_component_config FOREIGN KEY (component_id) REFERENCES public.component(id);


--
-- TOC entry 3700 (class 2606 OID 17602)
-- Name: component fk_component_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.component
    ADD CONSTRAINT fk_component_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3684 (class 2606 OID 17294)
-- Name: realm_default_groups fk_def_groups_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_default_groups
    ADD CONSTRAINT fk_def_groups_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3677 (class 2606 OID 17147)
-- Name: user_federation_mapper_config fk_fedmapper_cfg; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_mapper_config
    ADD CONSTRAINT fk_fedmapper_cfg FOREIGN KEY (user_federation_mapper_id) REFERENCES public.user_federation_mapper(id);


--
-- TOC entry 3676 (class 2606 OID 17142)
-- Name: user_federation_mapper fk_fedmapperpm_fedprv; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_mapper
    ADD CONSTRAINT fk_fedmapperpm_fedprv FOREIGN KEY (federation_provider_id) REFERENCES public.user_federation_provider(id);


--
-- TOC entry 3675 (class 2606 OID 17137)
-- Name: user_federation_mapper fk_fedmapperpm_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_mapper
    ADD CONSTRAINT fk_fedmapperpm_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3698 (class 2606 OID 17519)
-- Name: associated_policy fk_frsr5s213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.associated_policy
    ADD CONSTRAINT fk_frsr5s213xcx4wnkog82ssrfy FOREIGN KEY (associated_policy_id) REFERENCES public.resource_server_policy(id);


--
-- TOC entry 3696 (class 2606 OID 17504)
-- Name: scope_policy fk_frsrasp13xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.scope_policy
    ADD CONSTRAINT fk_frsrasp13xcx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- TOC entry 3704 (class 2606 OID 17887)
-- Name: resource_server_perm_ticket fk_frsrho213xcx4wnkog82sspmt; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrho213xcx4wnkog82sspmt FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- TOC entry 3687 (class 2606 OID 17730)
-- Name: resource_server_resource fk_frsrho213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_resource
    ADD CONSTRAINT fk_frsrho213xcx4wnkog82ssrfy FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- TOC entry 3705 (class 2606 OID 17892)
-- Name: resource_server_perm_ticket fk_frsrho213xcx4wnkog83sspmt; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrho213xcx4wnkog83sspmt FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- TOC entry 3706 (class 2606 OID 17897)
-- Name: resource_server_perm_ticket fk_frsrho213xcx4wnkog84sspmt; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrho213xcx4wnkog84sspmt FOREIGN KEY (scope_id) REFERENCES public.resource_server_scope(id);


--
-- TOC entry 3697 (class 2606 OID 17514)
-- Name: associated_policy fk_frsrpas14xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.associated_policy
    ADD CONSTRAINT fk_frsrpas14xcx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- TOC entry 3695 (class 2606 OID 17499)
-- Name: scope_policy fk_frsrpass3xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.scope_policy
    ADD CONSTRAINT fk_frsrpass3xcx4wnkog82ssrfy FOREIGN KEY (scope_id) REFERENCES public.resource_server_scope(id);


--
-- TOC entry 3707 (class 2606 OID 17920)
-- Name: resource_server_perm_ticket fk_frsrpo2128cx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrpo2128cx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- TOC entry 3689 (class 2606 OID 17725)
-- Name: resource_server_policy fk_frsrpo213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_policy
    ADD CONSTRAINT fk_frsrpo213xcx4wnkog82ssrfy FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- TOC entry 3691 (class 2606 OID 17469)
-- Name: resource_scope fk_frsrpos13xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_scope
    ADD CONSTRAINT fk_frsrpos13xcx4wnkog82ssrfy FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- TOC entry 3693 (class 2606 OID 17484)
-- Name: resource_policy fk_frsrpos53xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_policy
    ADD CONSTRAINT fk_frsrpos53xcx4wnkog82ssrfy FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- TOC entry 3694 (class 2606 OID 17489)
-- Name: resource_policy fk_frsrpp213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_policy
    ADD CONSTRAINT fk_frsrpp213xcx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- TOC entry 3692 (class 2606 OID 17474)
-- Name: resource_scope fk_frsrps213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_scope
    ADD CONSTRAINT fk_frsrps213xcx4wnkog82ssrfy FOREIGN KEY (scope_id) REFERENCES public.resource_server_scope(id);


--
-- TOC entry 3688 (class 2606 OID 17735)
-- Name: resource_server_scope fk_frsrso213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_server_scope
    ADD CONSTRAINT fk_frsrso213xcx4wnkog82ssrfy FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- TOC entry 3640 (class 2606 OID 16705)
-- Name: composite_role fk_gr7thllb9lu8q4vqa4524jjy8; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.composite_role
    ADD CONSTRAINT fk_gr7thllb9lu8q4vqa4524jjy8 FOREIGN KEY (child_role) REFERENCES public.keycloak_role(id);


--
-- TOC entry 3703 (class 2606 OID 17862)
-- Name: user_consent_client_scope fk_grntcsnt_clsc_usc; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_consent_client_scope
    ADD CONSTRAINT fk_grntcsnt_clsc_usc FOREIGN KEY (user_consent_id) REFERENCES public.user_consent(id);


--
-- TOC entry 3669 (class 2606 OID 16995)
-- Name: user_consent fk_grntcsnt_user; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_consent
    ADD CONSTRAINT fk_grntcsnt_user FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 3682 (class 2606 OID 17268)
-- Name: group_attribute fk_group_attribute_group; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.group_attribute
    ADD CONSTRAINT fk_group_attribute_group FOREIGN KEY (group_id) REFERENCES public.keycloak_group(id);


--
-- TOC entry 3681 (class 2606 OID 17282)
-- Name: group_role_mapping fk_group_role_group; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.group_role_mapping
    ADD CONSTRAINT fk_group_role_group FOREIGN KEY (group_id) REFERENCES public.keycloak_group(id);


--
-- TOC entry 3666 (class 2606 OID 16939)
-- Name: realm_enabled_event_types fk_h846o4h0w8epx5nwedrf5y69j; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_enabled_event_types
    ADD CONSTRAINT fk_h846o4h0w8epx5nwedrf5y69j FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3644 (class 2606 OID 16715)
-- Name: realm_events_listeners fk_h846o4h0w8epx5nxev9f5y69j; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_events_listeners
    ADD CONSTRAINT fk_h846o4h0w8epx5nxev9f5y69j FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3667 (class 2606 OID 16985)
-- Name: identity_provider_mapper fk_idpm_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.identity_provider_mapper
    ADD CONSTRAINT fk_idpm_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3668 (class 2606 OID 17161)
-- Name: idp_mapper_config fk_idpmconfig; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.idp_mapper_config
    ADD CONSTRAINT fk_idpmconfig FOREIGN KEY (idp_mapper_id) REFERENCES public.identity_provider_mapper(id);


--
-- TOC entry 3654 (class 2606 OID 16725)
-- Name: web_origins fk_lojpho213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.web_origins
    ADD CONSTRAINT fk_lojpho213xcx4wnkog82ssrfy FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- TOC entry 3648 (class 2606 OID 16735)
-- Name: scope_mapping fk_ouse064plmlr732lxjcn1q5f1; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.scope_mapping
    ADD CONSTRAINT fk_ouse064plmlr732lxjcn1q5f1 FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- TOC entry 3658 (class 2606 OID 16878)
-- Name: protocol_mapper fk_pcm_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.protocol_mapper
    ADD CONSTRAINT fk_pcm_realm FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- TOC entry 3641 (class 2606 OID 16750)
-- Name: credential fk_pfyr0glasqyl0dei3kl69r6v0; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.credential
    ADD CONSTRAINT fk_pfyr0glasqyl0dei3kl69r6v0 FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 3660 (class 2606 OID 17154)
-- Name: protocol_mapper_config fk_pmconfig; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.protocol_mapper_config
    ADD CONSTRAINT fk_pmconfig FOREIGN KEY (protocol_mapper_id) REFERENCES public.protocol_mapper(id);


--
-- TOC entry 3702 (class 2606 OID 17847)
-- Name: default_client_scope fk_r_def_cli_scope_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.default_client_scope
    ADD CONSTRAINT fk_r_def_cli_scope_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3680 (class 2606 OID 17199)
-- Name: required_action_provider fk_req_act_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.required_action_provider
    ADD CONSTRAINT fk_req_act_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3709 (class 2606 OID 17928)
-- Name: resource_uris fk_resource_server_uris; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.resource_uris
    ADD CONSTRAINT fk_resource_server_uris FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- TOC entry 3710 (class 2606 OID 17943)
-- Name: role_attribute fk_role_attribute_id; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.role_attribute
    ADD CONSTRAINT fk_role_attribute_id FOREIGN KEY (role_id) REFERENCES public.keycloak_role(id);


--
-- TOC entry 3664 (class 2606 OID 16908)
-- Name: realm_supported_locales fk_supported_locales_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.realm_supported_locales
    ADD CONSTRAINT fk_supported_locales_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- TOC entry 3650 (class 2606 OID 16770)
-- Name: user_federation_config fk_t13hpu1j94r2ebpekr39x5eu5; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_federation_config
    ADD CONSTRAINT fk_t13hpu1j94r2ebpekr39x5eu5 FOREIGN KEY (user_federation_provider_id) REFERENCES public.user_federation_provider(id);


--
-- TOC entry 3683 (class 2606 OID 17275)
-- Name: user_group_membership fk_user_group_user; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.user_group_membership
    ADD CONSTRAINT fk_user_group_user FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 3690 (class 2606 OID 17459)
-- Name: policy_config fkdc34197cf864c4e43; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.policy_config
    ADD CONSTRAINT fkdc34197cf864c4e43 FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- TOC entry 3663 (class 2606 OID 16888)
-- Name: identity_provider_config fkdc4897cf864c4e43; Type: FK CONSTRAINT; Schema: public; Owner: keycloak_user
--

ALTER TABLE ONLY public.identity_provider_config
    ADD CONSTRAINT fkdc4897cf864c4e43 FOREIGN KEY (identity_provider_id) REFERENCES public.identity_provider(internal_id);

CREATE USER user_university WITH PASSWORD '1234' CREATEDB;
CREATE DATABASE university
    WITH 
    OWNER = user_university
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

\connect university

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 201 (class 1259 OID 18009)
-- Name: classrooms; Type: TABLE; Schema: public; Owner: user_university
--

CREATE TABLE public.classrooms (
    id integer NOT NULL,
    name character varying(255)
);


ALTER TABLE public.classrooms OWNER TO user_university;

--
-- TOC entry 200 (class 1259 OID 18007)
-- Name: classrooms_id_seq; Type: SEQUENCE; Schema: public; Owner: user_university
--

ALTER TABLE public.classrooms ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.classrooms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 203 (class 1259 OID 18016)
-- Name: courses; Type: TABLE; Schema: public; Owner: user_university
--

CREATE TABLE public.courses (
    id integer NOT NULL,
    name character varying(255),
    teacher integer
);


ALTER TABLE public.courses OWNER TO user_university;

--
-- TOC entry 202 (class 1259 OID 18014)
-- Name: courses_id_seq; Type: SEQUENCE; Schema: public; Owner: user_university
--

ALTER TABLE public.courses ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.courses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 205 (class 1259 OID 18023)
-- Name: groups; Type: TABLE; Schema: public; Owner: user_university
--

CREATE TABLE public.groups (
    id integer NOT NULL,
    name character varying(255)
);


ALTER TABLE public.groups OWNER TO user_university;

--
-- TOC entry 204 (class 1259 OID 18021)
-- Name: groups_id_seq; Type: SEQUENCE; Schema: public; Owner: user_university
--

ALTER TABLE public.groups ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 210 (class 1259 OID 18051)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: user_university
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO user_university;

--
-- TOC entry 207 (class 1259 OID 18030)
-- Name: lessons; Type: TABLE; Schema: public; Owner: user_university
--

CREATE TABLE public.lessons (
    id integer NOT NULL,
    duration bigint,
    starttime bigint,
    classroom_id integer,
    course_id integer,
    group_id integer
);


ALTER TABLE public.lessons OWNER TO user_university;

--
-- TOC entry 206 (class 1259 OID 18028)
-- Name: lessons_id_seq; Type: SEQUENCE; Schema: public; Owner: user_university
--

ALTER TABLE public.lessons ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.lessons_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 208 (class 1259 OID 18035)
-- Name: students; Type: TABLE; Schema: public; Owner: user_university
--

CREATE TABLE public.students (
    id integer NOT NULL,
    firstname character varying(255),
    lastname character varying(255),
    group_id integer
);


ALTER TABLE public.students OWNER TO user_university;

--
-- TOC entry 209 (class 1259 OID 18043)
-- Name: teachers; Type: TABLE; Schema: public; Owner: user_university
--

CREATE TABLE public.teachers (
    id integer NOT NULL,
    firstname character varying(255),
    lastname character varying(255)
);


ALTER TABLE public.teachers OWNER TO user_university;

--
-- TOC entry 3030 (class 0 OID 18009)
-- Dependencies: 201
-- Data for Name: classrooms; Type: TABLE DATA; Schema: public; Owner: user_university
--

INSERT INTO public.classrooms (id, name) VALUES (1, 'ROOM-1');
INSERT INTO public.classrooms (id, name) VALUES (2, 'ROOM-2');
INSERT INTO public.classrooms (id, name) VALUES (3, 'ROOM-3');
INSERT INTO public.classrooms (id, name) VALUES (4, 'ROOM-4');
INSERT INTO public.classrooms (id, name) VALUES (5, 'ROOM-5');
INSERT INTO public.classrooms (id, name) VALUES (6, 'ROOM-6');
INSERT INTO public.classrooms (id, name) VALUES (7, 'ROOM-7');
INSERT INTO public.classrooms (id, name) VALUES (8, 'ROOM-8');
INSERT INTO public.classrooms (id, name) VALUES (9, 'ROOM-9');
INSERT INTO public.classrooms (id, name) VALUES (10, 'ROOM-10');
INSERT INTO public.classrooms (id, name) VALUES (11, 'ROOM-11');
INSERT INTO public.classrooms (id, name) VALUES (12, 'ROOM-12');
INSERT INTO public.classrooms (id, name) VALUES (13, 'ROOM-13');
INSERT INTO public.classrooms (id, name) VALUES (14, 'ROOM-14');
INSERT INTO public.classrooms (id, name) VALUES (15, 'ROOM-15');
INSERT INTO public.classrooms (id, name) VALUES (16, 'ROOM-16');
INSERT INTO public.classrooms (id, name) VALUES (17, 'ROOM-17');


--
-- TOC entry 3032 (class 0 OID 18016)
-- Dependencies: 203
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: user_university
--

INSERT INTO public.courses (id, name, teacher) VALUES (1, 'Advertising business', 1);
INSERT INTO public.courses (id, name, teacher) VALUES (2, 'Management', 2);
INSERT INTO public.courses (id, name, teacher) VALUES (3, 'Communications', 3);
INSERT INTO public.courses (id, name, teacher) VALUES (4, 'English', 4);
INSERT INTO public.courses (id, name, teacher) VALUES (5, 'Marketing', 5);
INSERT INTO public.courses (id, name, teacher) VALUES (6, 'Media Studies', 6);
INSERT INTO public.courses (id, name, teacher) VALUES (7, 'Graphic Design', 7);
INSERT INTO public.courses (id, name, teacher) VALUES (8, 'Fine Art', 8);
INSERT INTO public.courses (id, name, teacher) VALUES (9, 'Hospitality management', 9);
INSERT INTO public.courses (id, name, teacher) VALUES (10, 'Leisure and Tourism', 10);
INSERT INTO public.courses (id, name, teacher) VALUES (11, 'Building management', 11);
INSERT INTO public.courses (id, name, teacher) VALUES (12, 'Business Studies', 12);
INSERT INTO public.courses (id, name, teacher) VALUES (13, 'Construction Technology', 13);
INSERT INTO public.courses (id, name, teacher) VALUES (14, 'Archaeology', 14);
INSERT INTO public.courses (id, name, teacher) VALUES (15, 'Social Education', 15);
INSERT INTO public.courses (id, name, teacher) VALUES (16, 'Geography', 16);
INSERT INTO public.courses (id, name, teacher) VALUES (17, 'Museum Studies', 17);
INSERT INTO public.courses (id, name, teacher) VALUES (18, 'History', 18);


--
-- TOC entry 3034 (class 0 OID 18023)
-- Dependencies: 205
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: user_university
--

INSERT INTO public.groups (id, name) VALUES (1, 'AB-101');
INSERT INTO public.groups (id, name) VALUES (2, 'AB-201');
INSERT INTO public.groups (id, name) VALUES (3, 'AB-301');
INSERT INTO public.groups (id, name) VALUES (4, 'AB-401');
INSERT INTO public.groups (id, name) VALUES (5, 'AB-501');
INSERT INTO public.groups (id, name) VALUES (6, 'CD-101');
INSERT INTO public.groups (id, name) VALUES (7, 'CD-201');
INSERT INTO public.groups (id, name) VALUES (8, 'CD-301');
INSERT INTO public.groups (id, name) VALUES (9, 'CD-401');
INSERT INTO public.groups (id, name) VALUES (10, 'CD-501');


--
-- TOC entry 3036 (class 0 OID 18030)
-- Dependencies: 207
-- Data for Name: lessons; Type: TABLE DATA; Schema: public; Owner: user_university
--

INSERT INTO public.lessons (id, duration, starttime, classroom_id, course_id, group_id) VALUES (1, 2700000, 1616510000000, 1, 1, 1);
INSERT INTO public.lessons (id, duration, starttime, classroom_id, course_id, group_id) VALUES (2, 3600000, 1616510000000, 2, 2, 2);


--
-- TOC entry 3037 (class 0 OID 18035)
-- Dependencies: 208
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: user_university
--

INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (1, 'Abbey', 'Wilkes', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (2, 'Bernice', 'Bone', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (3, 'Cecelia', 'Ferreira', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (4, 'Natan', 'Valencia', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (5, 'Olivia', 'Fitzpatrick', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (6, 'Aimie', 'Morrow', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (7, 'Joel', 'West', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (8, 'Lola-Mae', 'Mac', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (9, 'Jaxx', 'Hahn', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (10, 'Emma', 'Byrd', 1);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (11, 'Blythe', 'Guthrie', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (12, 'Ocean', 'Roberson', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (13, 'Jac', 'Crosby', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (14, 'Kadeem', 'Vance', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (15, 'Michalina', 'Shannon', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (16, 'Davey', 'Hume', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (17, 'Faheem', 'Cox', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (18, 'Eden', 'Keeling', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (19, 'Tasnia', 'Glass', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (20, 'Coby', 'Barlow', 2);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (21, 'Devante', 'Rosario', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (22, 'Jovan', 'Rhodes', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (23, 'Tomi', 'Ridley', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (24, 'Danish', 'Philip', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (25, 'Fiona', 'Mill', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (26, 'Matylda', 'Rosas', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (27, 'Alessia', 'Huffman', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (28, 'Nell', 'Wilkerson', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (29, 'Miranda', 'Shelton', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (30, 'Harvey-Lee', 'Duffy', 3);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (31, 'Artur', 'Hook', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (32, 'Isaac', 'Laing', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (33, 'Aled', 'Marin', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (34, 'Shantelle', 'Ochoa', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (35, 'Etienne', 'Xiong', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (36, 'Andy', 'Wilson', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (37, 'Ruby', 'Hammond', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (38, 'Eshaal', 'Novak', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (39, 'Aedan', 'North', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (40, 'Isra', 'Allan', 4);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (41, 'Isla-Rae', 'Davis', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (42, 'Victoria', 'Barajas', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (43, 'Frances', 'Mcgregor', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (44, 'Kenny', 'Mckeown', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (45, 'Haniya', 'Watson', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (46, 'Kiah', 'Farrington', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (47, 'Izzie', 'Carrillo', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (48, 'Alysia', 'Russell', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (49, 'Sabina', 'Barker', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (50, 'Aida', 'Weir', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (51, 'Glenda', 'Bond', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (52, 'River', 'Mcguire', 5);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (53, 'Shaurya', 'Moran', 6);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (54, 'Delilah', 'Terry', 6);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (55, 'Milo', 'Whyte', 6);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (56, 'Safiyyah', 'Mccarty', 6);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (57, 'Livia', 'Lucero', 6);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (58, 'Albie', 'Christensen', 6);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (59, 'Nisha', 'Winters', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (60, 'Jillian', 'Tomlinson', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (61, 'Ksawery', 'Osborn', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (62, 'Huda', 'Compton', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (63, 'Sian', 'Gould', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (64, 'Sheridan', 'Esquivel', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (65, 'Anees', 'Quinn', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (66, 'Kornelia', 'Rose', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (67, 'Yousif', 'Clements', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (68, 'Naomi', 'Hamilton', 7);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (69, 'Kasey', 'Serrano', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (70, 'Cathal', 'Stephens', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (71, 'Tehya', 'Douglas', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (72, 'Marisa', 'Garrett', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (73, 'Carrie', 'Briggs', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (74, 'Caio', 'Blevins', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (75, 'Ann', 'Carpenter', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (76, 'Farhana', 'Brennan', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (77, 'Lillie-May', 'Ho', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (78, 'Ariana', 'Hoffman', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (79, 'Shakira', 'Santos', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (80, 'Leen', 'Salgado', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (81, 'Misty', 'Mcbride', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (82, 'Amaya', 'Vo', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (83, 'Jannah', 'Moore', 8);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (84, 'Kelsey', 'Cowan', 9);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (85, 'Elleanor', 'Finch', 9);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (86, 'Niyah', 'Guerra', 9);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (87, 'Craig', 'Donald', 9);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (88, 'Asma', 'Frost', 9);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (89, 'Jawad', 'Carroll', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (90, 'Lincoln', 'Burn', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (91, 'Herman', 'Rangel', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (92, 'Presley', 'Mendoza', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (93, 'Jazmine', 'Paterson', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (94, 'Bradley', 'York', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (95, 'Fred', 'Harding', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (96, 'Mysha', 'Bowers', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (97, 'Erica', 'Corbett', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (98, 'Khadeejah', 'Gordon', 10);
INSERT INTO public.students (id, firstname, lastname, group_id) VALUES (99, 'Miya', 'Blake', 10);


--
-- TOC entry 3038 (class 0 OID 18043)
-- Dependencies: 209
-- Data for Name: teachers; Type: TABLE DATA; Schema: public; Owner: user_university
--

INSERT INTO public.teachers (id, firstname, lastname) VALUES (1, 'Bradleigh', 'Donaldson');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (2, 'Minahil', 'Heath');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (3, 'Garry', 'Mclellan');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (4, 'Anand', 'Whittington');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (5, 'Ioana', 'Ellwood');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (6, 'Odin', 'Combs');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (7, 'Jonas', 'Huang');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (8, 'Devonte', 'Appleton');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (9, 'Flynn', 'Michael');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (10, 'Wanda', 'Sherman');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (11, 'Stefania', 'Kelley');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (12, 'Junior', 'Tillman');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (13, 'Micah', 'Cowan');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (14, 'Chelsea', 'Clay');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (15, 'Manisha', 'Sykes');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (16, 'Nataniel', 'Kent');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (17, 'Miley', 'Maddox');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (18, 'Yuvaan', 'Dickerson');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (19, 'Xander', 'Kirkpatrick');
INSERT INTO public.teachers (id, firstname, lastname) VALUES (20, 'Avleen', 'Cochran');


--
-- TOC entry 3046 (class 0 OID 0)
-- Dependencies: 200
-- Name: classrooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user_university
--

SELECT pg_catalog.setval('public.classrooms_id_seq', 7, true);


--
-- TOC entry 3047 (class 0 OID 0)
-- Dependencies: 202
-- Name: courses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user_university
--

SELECT pg_catalog.setval('public.courses_id_seq', 1, false);


--
-- TOC entry 3048 (class 0 OID 0)
-- Dependencies: 204
-- Name: groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user_university
--

SELECT pg_catalog.setval('public.groups_id_seq', 1, true);


--
-- TOC entry 3049 (class 0 OID 0)
-- Dependencies: 210
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: user_university
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, true);


--
-- TOC entry 3050 (class 0 OID 0)
-- Dependencies: 206
-- Name: lessons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: user_university
--

SELECT pg_catalog.setval('public.lessons_id_seq', 1, false);


--
-- TOC entry 2883 (class 2606 OID 18013)
-- Name: classrooms classrooms_pkey; Type: CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.classrooms
    ADD CONSTRAINT classrooms_pkey PRIMARY KEY (id);


--
-- TOC entry 2885 (class 2606 OID 18020)
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (id);


--
-- TOC entry 2887 (class 2606 OID 18027)
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- TOC entry 2889 (class 2606 OID 18034)
-- Name: lessons lessons_pkey; Type: CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT lessons_pkey PRIMARY KEY (id);


--
-- TOC entry 2891 (class 2606 OID 18042)
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- TOC entry 2893 (class 2606 OID 18050)
-- Name: teachers teachers_pkey; Type: CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_pkey PRIMARY KEY (id);


--
-- TOC entry 2896 (class 2606 OID 18063)
-- Name: lessons fk17ucc7gjfjddsyi0gvstkqeat; Type: FK CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT fk17ucc7gjfjddsyi0gvstkqeat FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- TOC entry 2894 (class 2606 OID 18053)
-- Name: courses fkb100wekf2rnic4p3ehqbntttu; Type: FK CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT fkb100wekf2rnic4p3ehqbntttu FOREIGN KEY (teacher) REFERENCES public.teachers(id);


--
-- TOC entry 2895 (class 2606 OID 18058)
-- Name: lessons fkbffxqtymudjwdb39m7dnjn4ey; Type: FK CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT fkbffxqtymudjwdb39m7dnjn4ey FOREIGN KEY (classroom_id) REFERENCES public.classrooms(id);


--
-- TOC entry 2898 (class 2606 OID 18073)
-- Name: students fkmsev1nou0j86spuk5jrv19mss; Type: FK CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT fkmsev1nou0j86spuk5jrv19mss FOREIGN KEY (group_id) REFERENCES public.groups(id);


--
-- TOC entry 2897 (class 2606 OID 18068)
-- Name: lessons fktdolsaotaqlwxbxwaxt00kimk; Type: FK CONSTRAINT; Schema: public; Owner: user_university
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT fktdolsaotaqlwxbxwaxt00kimk FOREIGN KEY (group_id) REFERENCES public.groups(id); 
