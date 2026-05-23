--
-- PostgreSQL database dump
--

\restrict BeWfRu5MIc9FeNgWExG66nfPFK8LzDRuJzUQ3QFm7N3CAgkvE98hdwaYkf7hg1A

-- Dumped from database version 17.9 (Debian 17.9-1.pgdg12+1)
-- Dumped by pg_dump version 18.0

-- Started on 2026-05-13 23:17:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: root
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO root;

--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: root
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 863 (class 1247 OID 16399)
-- Name: payment_status; Type: TYPE; Schema: public; Owner: root
--

CREATE TYPE public.payment_status AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED'
);


ALTER TYPE public.payment_status OWNER TO root;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 218 (class 1259 OID 16406)
-- Name: admins; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.admins (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(50) NOT NULL
);


ALTER TABLE public.admins OWNER TO root;

--
-- TOC entry 217 (class 1259 OID 16405)
-- Name: admins_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.admins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.admins_id_seq OWNER TO root;

--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 217
-- Name: admins_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.admins_id_seq OWNED BY public.admins.id;


--
-- TOC entry 222 (class 1259 OID 16426)
-- Name: email_otp; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.email_otp (
    id bigint NOT NULL,
    email character varying(255),
    expiry_time timestamp without time zone,
    otp character varying(255),
    used boolean NOT NULL
);


ALTER TABLE public.email_otp OWNER TO root;

--
-- TOC entry 221 (class 1259 OID 16425)
-- Name: email_otp_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.email_otp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.email_otp_id_seq OWNER TO root;

--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 221
-- Name: email_otp_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.email_otp_id_seq OWNED BY public.email_otp.id;


--
-- TOC entry 224 (class 1259 OID 16435)
-- Name: job_contract; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.job_contract (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    contract_no integer NOT NULL,
    sr_no integer,
    contract_date date NOT NULL,
    weaver_name character varying(255) NOT NULL,
    trader_name character varying(255),
    broker_name character varying(255),
    quality character varying(255),
    quantity_meters integer,
    job_rate double precision,
    payment_days character varying(255),
    production_schedule character varying(255),
    no_of_machines integer,
    remark character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    beams character varying(255),
    cut_length character varying(255),
    minimum_delivery character varying(255),
    rolling_folding character varying(255),
    sizing_fabric character varying(255),
    amount double precision,
    brokerage_mtr_amt double precision,
    brokerage_percent_amt double precision,
    pick double precision,
    rate double precision,
    weaver_brokerage_paisa double precision,
    weaver_brokerage_percent double precision
);


ALTER TABLE public.job_contract OWNER TO root;

--
-- TOC entry 223 (class 1259 OID 16434)
-- Name: job_contract_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.job_contract_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.job_contract_id_seq OWNER TO root;

--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 223
-- Name: job_contract_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.job_contract_id_seq OWNED BY public.job_contract.id;


--
-- TOC entry 234 (class 1259 OID 16523)
-- Name: payments; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.payments (
    id bigint NOT NULL,
    admin_notes character varying(500),
    amount double precision,
    approval_date timestamp(6) without time zone,
    email character varying(150) NOT NULL,
    mobile_number character varying(10) NOT NULL,
    payment_date timestamp(6) without time zone NOT NULL,
    status character varying(255) NOT NULL,
    subscription_end_date date,
    subscription_start_date date,
    user_name character varying(100) NOT NULL,
    utr_number character varying(50) NOT NULL,
    CONSTRAINT payments_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying, 'NOT_FOUND'::character varying])::text[])))
);


ALTER TABLE public.payments OWNER TO root;

--
-- TOC entry 233 (class 1259 OID 16522)
-- Name: payments_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.payments_id_seq OWNER TO root;

--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 233
-- Name: payments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.payments_id_seq OWNED BY public.payments.id;


--
-- TOC entry 230 (class 1259 OID 16494)
-- Name: qualitymaster; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qualitymaster (
    id bigint NOT NULL,
    pick character varying(255) NOT NULL,
    qulity_name character varying(255) NOT NULL,
    reed character varying(255) NOT NULL,
    weave character varying(255) NOT NULL,
    weft character varying(255) NOT NULL,
    width character varying(255) NOT NULL,
    created_at timestamp without time zone,
    user_id bigint NOT NULL,
    warp character varying(255),
    alias character varying(255),
    reed_space character varying(255)
);


ALTER TABLE public.qualitymaster OWNER TO root;

--
-- TOC entry 229 (class 1259 OID 16493)
-- Name: qualitymaster_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.qualitymaster_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.qualitymaster_id_seq OWNER TO root;

--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 229
-- Name: qualitymaster_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.qualitymaster_id_seq OWNED BY public.qualitymaster.id;


--
-- TOC entry 220 (class 1259 OID 16415)
-- Name: users; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(150) NOT NULL,
    name character varying(100) NOT NULL,
    password character varying(255)
);


ALTER TABLE public.users OWNER TO root;

--
-- TOC entry 219 (class 1259 OID 16414)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO root;

--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 226 (class 1259 OID 16461)
-- Name: wallet; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.wallet (
    id bigint NOT NULL,
    balance double precision NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.wallet OWNER TO root;

--
-- TOC entry 225 (class 1259 OID 16460)
-- Name: wallet_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.wallet_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.wallet_id_seq OWNER TO root;

--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 225
-- Name: wallet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.wallet_id_seq OWNED BY public.wallet.id;


--
-- TOC entry 228 (class 1259 OID 16475)
-- Name: wallet_transactions; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.wallet_transactions (
    id bigint NOT NULL,
    amount double precision NOT NULL,
    created_at timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    remaining_bal double precision NOT NULL,
    transaction_type character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    wallet_id bigint NOT NULL
);


ALTER TABLE public.wallet_transactions OWNER TO root;

--
-- TOC entry 227 (class 1259 OID 16474)
-- Name: wallet_transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.wallet_transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.wallet_transactions_id_seq OWNER TO root;

--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 227
-- Name: wallet_transactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.wallet_transactions_id_seq OWNED BY public.wallet_transactions.id;


--
-- TOC entry 232 (class 1259 OID 16503)
-- Name: weaver_trader; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.weaver_trader (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    name character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    phno bigint NOT NULL,
    divanji_mob_no bigint,
    owner_mob_no bigint,
    weaver_brokerage_paisa double precision,
    weaver_brokerage_percent double precision
);


ALTER TABLE public.weaver_trader OWNER TO root;

--
-- TOC entry 231 (class 1259 OID 16502)
-- Name: weaver_trader_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.weaver_trader_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.weaver_trader_id_seq OWNER TO root;

--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 231
-- Name: weaver_trader_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.weaver_trader_id_seq OWNED BY public.weaver_trader.id;


--
-- TOC entry 3257 (class 2604 OID 16409)
-- Name: admins id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.admins ALTER COLUMN id SET DEFAULT nextval('public.admins_id_seq'::regclass);


--
-- TOC entry 3259 (class 2604 OID 16429)
-- Name: email_otp id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.email_otp ALTER COLUMN id SET DEFAULT nextval('public.email_otp_id_seq'::regclass);


--
-- TOC entry 3260 (class 2604 OID 16438)
-- Name: job_contract id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.job_contract ALTER COLUMN id SET DEFAULT nextval('public.job_contract_id_seq'::regclass);


--
-- TOC entry 3267 (class 2604 OID 16526)
-- Name: payments id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.payments ALTER COLUMN id SET DEFAULT nextval('public.payments_id_seq'::regclass);


--
-- TOC entry 3265 (class 2604 OID 16497)
-- Name: qualitymaster id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qualitymaster ALTER COLUMN id SET DEFAULT nextval('public.qualitymaster_id_seq'::regclass);


--
-- TOC entry 3258 (class 2604 OID 16418)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3263 (class 2604 OID 16464)
-- Name: wallet id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet ALTER COLUMN id SET DEFAULT nextval('public.wallet_id_seq'::regclass);


--
-- TOC entry 3264 (class 2604 OID 16478)
-- Name: wallet_transactions id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet_transactions ALTER COLUMN id SET DEFAULT nextval('public.wallet_transactions_id_seq'::regclass);


--
-- TOC entry 3266 (class 2604 OID 16506)
-- Name: weaver_trader id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.weaver_trader ALTER COLUMN id SET DEFAULT nextval('public.weaver_trader_id_seq'::regclass);


--
-- TOC entry 3448 (class 0 OID 16406)
-- Dependencies: 218
-- Data for Name: admins; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.admins VALUES (1, 'Administrator', '$2a$10$LqxlBzmA/wXhCUlBzwfRkutk9j.ywYsMEKSX3F9idKhyHtZx9/Xnu', 'admin');


--
-- TOC entry 3452 (class 0 OID 16426)
-- Dependencies: 222
-- Data for Name: email_otp; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.email_otp VALUES (1, 'riteshchougule96@gmail.com', '2026-04-18 05:45:13.317131', '445281', true);
INSERT INTO public.email_otp VALUES (2, 'akankshapawar8866@gmail.com', '2026-04-18 06:12:51.35469', '357565', true);
INSERT INTO public.email_otp VALUES (3, 'shekharpatil4545@gmail.com', '2026-04-18 06:32:15.210623', '700662', false);
INSERT INTO public.email_otp VALUES (4, 'koparde.shritej111@gmail.com', '2026-04-18 06:42:20.422536', '135032', true);
INSERT INTO public.email_otp VALUES (5, 'ritesh.chougule@smartrecontech.com', '2026-04-18 15:48:13.143175', '138470', true);
INSERT INTO public.email_otp VALUES (6, 'riteshchougule96@gmail.com', '2026-04-18 15:53:18.988067', '622326', false);
INSERT INTO public.email_otp VALUES (7, 'riteshchougule96@gmail.com', '2026-04-18 15:53:19.841363', '939115', false);
INSERT INTO public.email_otp VALUES (8, 'ritesh.chougule@smartrecontech.com', '2026-04-22 00:10:02.007162', '632747', true);
INSERT INTO public.email_otp VALUES (9, 'akankshapawar8866@gmail.com', '2026-04-22 00:14:18.592833', '309820', true);
INSERT INTO public.email_otp VALUES (10, 'ritesh123@gmail.com', '2026-04-22 00:21:05.067886', '428832', false);
INSERT INTO public.email_otp VALUES (11, 'ritesh123@gmail.com', '2026-04-22 00:23:38.795228', '192615', false);
INSERT INTO public.email_otp VALUES (12, 'akankshapawar8866@gmail.com', '2026-04-22 09:46:06.655311', '116052', true);
INSERT INTO public.email_otp VALUES (13, 'riteshchougule969@gmail.com', '2026-04-22 23:00:16.258669', '328826', false);
INSERT INTO public.email_otp VALUES (14, 'riteshchougule969@gmail.com', '2026-04-22 23:03:08.515109', '769150', false);
INSERT INTO public.email_otp VALUES (15, 'riteshchougule969@gmail.com', '2026-04-22 23:03:09.796679', '485349', false);
INSERT INTO public.email_otp VALUES (16, 'riteshchougule969@gmail.com', '2026-04-22 23:03:10.693991', '374909', false);
INSERT INTO public.email_otp VALUES (17, 'riteshchougule969@gmail.com', '2026-04-22 23:03:11.049936', '858123', false);
INSERT INTO public.email_otp VALUES (18, 'riteshchougule969@gmail.com', '2026-04-22 23:03:11.200464', '226221', false);
INSERT INTO public.email_otp VALUES (19, 'riteshchougule969@gmail.com', '2026-04-22 23:03:12.779376', '638734', false);
INSERT INTO public.email_otp VALUES (20, 'riteshchougule969@gmail.com', '2026-04-22 23:03:12.751828', '537974', false);
INSERT INTO public.email_otp VALUES (22, 'chetanhingmire1819@gmail.com', '2026-04-22 19:48:00.327261', '929803', false);
INSERT INTO public.email_otp VALUES (23, 'chetanhingmire1819@gmail.com', '2026-04-22 19:48:05.614416', '993895', false);
INSERT INTO public.email_otp VALUES (24, 'chetanhingmire1819@gmail.com', '2026-04-22 19:48:08.153001', '666277', false);
INSERT INTO public.email_otp VALUES (21, 'chetanhingmire1819@gmail.com', '2026-04-22 19:47:58.826341', '884353', true);
INSERT INTO public.email_otp VALUES (25, 'chougulera999@gmail.com', '2026-04-24 17:31:22.48708', '891880', true);
INSERT INTO public.email_otp VALUES (26, 'sheshnalwar@gmail.com', '2026-04-25 10:10:30.441665', '329419', true);
INSERT INTO public.email_otp VALUES (27, 'ritesh.chougule@smartrecontech.com', '2026-04-25 16:25:28.359248', '781288', true);
INSERT INTO public.email_otp VALUES (28, 'siddharthdoshi53@gmail.com', '2026-04-29 19:05:29.1925', '582884', true);
INSERT INTO public.email_otp VALUES (29, 'koparde.shritej111@gmail.com', '2026-05-02 10:19:52.989157', '795360', true);
INSERT INTO public.email_otp VALUES (30, 'riteshchougule96@gmail.com', '2026-05-02 19:50:51.990073', '683653', true);
INSERT INTO public.email_otp VALUES (31, 'QE@BFEB.COM', '2026-05-12 16:28:35.927526', '905318', false);
INSERT INTO public.email_otp VALUES (32, 'eglxh97183@minitts.net', '2026-05-12 16:31:45.627437', '745894', true);
INSERT INTO public.email_otp VALUES (33, 'telsingemayur@gmail.com', '2026-05-13 12:16:54.250548', '814108', true);


--
-- TOC entry 3454 (class 0 OID 16435)
-- Dependencies: 224
-- Data for Name: job_contract; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.job_contract VALUES (4, 1, 2, 2, '2026-04-18', 'Ritesh', 'Chougule', 'Ritesh Chougule', '90*90 / 90*90 / 90" / plain', 12000, 0.15, '60', '15', 18, 'Fish kg', '2026-04-18 07:15:36.087061', '2026-04-18 07:18:40.486591', '10', '10', '20', 'Rolling', 'SIZING,', 1620, 26.4, 4.37, 90, 0.14, 0.22, 0.27);
INSERT INTO public.job_contract VALUES (6, 1, 4, 4, '2026-04-25', 'Ritesh', 'Chougule', 'Ritesh Chougule', '90*90 / 90*90 / 90" / plain', 12000, 0.16, '60', '60', 18, 'Fish kg', '2026-04-24 18:32:41.719792', '2026-04-24 18:36:29.521359', '50', '110', '500', 'Rolling', 'SIZING', 1728, 26.4, 4.67, 90, 0.14, 0.22, 0.27);
INSERT INTO public.job_contract VALUES (1, 20, 1, 1, '2026-04-25', 'Ritesh', 'Chougule', 'Akanksha', '98*10 / 58 * 23 / 64" - Hard', 100, 10, '10', '10', 10, '10', '2026-04-17 23:18:46.742197', '2026-04-25 22:00:18.145347', '100', '10', '10', 'Folding', 'FABRIC', 230, 90, 20.7, 23, 2.3, 90, 9);
INSERT INTO public.job_contract VALUES (18, 1, 154, 13, '2026-05-13', 'Padale Group', 'Pm Vastra Udyog ', 'Ritesh Chougule', '40 PV*40PV / 84*68 / 62" / Plain', 0, 13, '1.50 % CD =20 days ', 'Regular', 3, 'Plain + Jacquard monogram ', '2026-05-13 06:06:35.772859', '2026-05-13 06:06:35.772864', '3', '-', '-', 'Folding', 'FABRIC', 'NaN', 'NaN', 'NaN', 68, 8.84, 0, 1);
INSERT INTO public.job_contract VALUES (8, 20, 3, 3, '2026-05-03', 'Ritesh', 'Chougule', 'Akanksha', '98*10 / 58 * 23 / 64" - Hard', 2000, 10293, '15-20 days 1.50 % CD', '10+10', 10, 'good', '2026-04-29 20:07:04.579388', '2026-05-03 15:00:26.761977', '10+20', '-', '-', 'Rolling', 'SIZING', 4734780, 1800, 426130.2, 23, 2367.39, 90, 9);
INSERT INTO public.job_contract VALUES (3, 1, 1, 1, '2026-04-26', 'Onkar Mete ', 'Pm Vastra Udyog ', 'Ritesh Chougule', '40pv*2/65+40pv / 88*70 / 62"" / Plain + Jacquard ', 25000, 14, '60', 'Regular', 2, '1100 mm beam / sizing - shivshakti ', '2026-04-18 05:49:35.503996', '2026-04-26 13:10:48.285263', '4', '- ', '- ', 'Folding', 'SIZING', 245000, 0, 0, 70, 9.8, 0, 0);
INSERT INTO public.job_contract VALUES (2, 20, 2, 2, '2026-04-29', 'Ritesh', 'Chougule', 'Akanksha', '98*10 / 58 * 23 / 64" - Hard', 10, 10, '10', '10', 10, '10', '2026-04-17 23:22:26.020913', '2026-04-29 19:56:52.256783', '10 + 10', '10', '10', 'Rolling', 'SIZING', 23, 9, 2.07, 23, 2.3, 90, 9);
INSERT INTO public.job_contract VALUES (7, 1, 5, 5, '2026-04-30', 'Onkar Mete ', 'Pm Vastra Udyog ', 'Ritesh Chougule', '40 PV*40PV / 84*68 / 62" / Plain', 40000, 12, '60 days 15-dc', 'Regular', 3, 'Rolling', '2026-04-25 05:25:43.05007', '2026-04-30 04:04:52.13785', '3+3', '-', '-', 'Rolling', 'SIZING', 326400, 0, 3264, 68, 8.16, 0, 1);
INSERT INTO public.job_contract VALUES (10, 1, 7, 6, '2026-05-02', 'Bhangadia Group ', 'Pankaj  Loya', 'Ritesh Chougule', '90*90 / 90*90 / 90" / plain', 16000, 0.15, '60', '60 days', 10, 'Good clothes', '2026-05-02 10:26:58.991905', '2026-05-02 10:28:38.953928', '3', '110', '150', 'Rolling', 'SIZING', 2160, 0, 21.6, 90, 0.14, 0, 1);
INSERT INTO public.job_contract VALUES (19, 1, 156, 14, '2026-05-13', 'Bhangadia Group ', 'Siddhivinayak Textiles ', 'Ritesh Chougule', '62" 80*60 / 30+30slub * 30+30slub - Jacquard monogram', 30000, 13, '60 days', 'Urgent ', 3, 'Plain + Jacquard monogram ', '2026-05-13 06:29:43.315409', '2026-05-13 06:29:43.315417', '3+3', '-', '-', 'Folding', 'SIZING', 234000, 0, 2340, 60, 7.8, 0, 1);
INSERT INTO public.job_contract VALUES (11, 1, 8, 7, '2026-05-03', 'Bhangadia Group ', 'Chougule', 'Ritesh Chougule', '33 karded compact *240 poly lycra  / 4/64*68 / 74" / 5/1', 50000, 40, '10-20', '60 days full ', 10, 'Good clothes  plain + jugakard monogram ', '2026-05-02 10:31:03.506139', '2026-05-03 10:16:43.356716', '3+3', '100', '10', 'Rolling', 'SIZING', 1360000, 0, 13600, 68, 27.2, 0, 1);
INSERT INTO public.job_contract VALUES (20, 6, 201, 1, '2026-05-13', 'Fine Fab, Akhsayji Rathi', 'Rohitji Karva', 'Mayur Telsinge', '62" 120*72 / 80d. * 40 cotton  - Plain', 25000, 11.5, '60 days', 'Urgent ', 3, 'Rolling', '2026-05-13 12:20:14.120336', '2026-05-13 12:20:14.120342', '3', '-', '-', 'Rolling', 'SIZING', 207000, 0, 2070, 72, 8.28, 0, 1);
INSERT INTO public.job_contract VALUES (12, 1, 9, 8, '2026-05-03', 'Onkar Mete ', 'Pm Vastra Udyog ', 'Ritesh Chougule', '65" 66/2*60 / 40 * 40 - Plain', 12000, 14, '60', '60 days full ', 10, 'Shree', '2026-05-03 14:03:53.143833', '2026-05-03 17:18:54.777773', '3+3', '100', '500', 'Rolling', 'SIZING', 100800, 0, 0, 60, 8.4, 0, 0);
INSERT INTO public.job_contract VALUES (21, 6, 202, 2, '2026-05-13', 'Sachinji Nimankar', 'Pm Vastra Udyog ', 'Mayur Telsinge', '62" 120*72 / 80d. * 40pc - Plain', 25000, 11.5, 'CD = 20 days', 'Urgent ', 3, 'Rolling', '2026-05-13 14:26:23.209329', '2026-05-13 14:26:23.209336', '3', '-', '-', 'Rolling', 'SIZING', 207000, 0, 2070, 72, 8.28, 0, 1);
INSERT INTO public.job_contract VALUES (22, 6, 203, 3, '2026-05-13', 'Kapilji Kharge', 'Mundra N Mundra ', 'Mayur Telsinge', 'RS 68" 4/64*90 / 50 cpt  * 50 cpt - Dobby as per paper', 40000, 12.75, '60 days', 'Urgent ', 4, 'Dobby as per paper ', '2026-05-13 15:04:25.762375', '2026-05-13 15:06:49.057877', '4+4', '-', '-', 'Rolling', 'SIZING', 459000, 0, 4590, 90, 11.47, 0, 1);
INSERT INTO public.job_contract VALUES (9, 20, 4, 4, '2026-05-13', 'Ritesh', 'Chougule', 'Akanksha', '98*10 / 58 * 23 / 64" - Hard', 2500, 12, '15-20 days 1.50 % CD', '10', 10, 'good', '2026-04-29 20:20:23.337886', '2026-05-13 07:54:07.891695', '10+20', '-', '-', 'Folding', 'FABRIC', 6900, 2250, 621, 23, 2.76, 90, 9);
INSERT INTO public.job_contract VALUES (5, 1, 3, 3, '2026-05-12', 'Ritesh', 'Chougule', 'Ritesh Chougule', '90*90 / 90*90 / 90" / plain', 10, 16, '60', '60', 10, 'Hi', '2026-04-18 07:26:45.61659', '2026-05-12 14:07:33.732432', '50', '110', '500', 'Rolling', 'SIZING', 144, 0.02, 0.39, 90, 14.4, 0.22, 0.27);
INSERT INTO public.job_contract VALUES (14, 20, 5, 5, '2026-05-13', 'Ritesh', 'Chougule', 'Akanksha', '98*10 / 58 * 23 / 64" - Hard', 10, 10, '10', '10', 10, '10', '2026-05-13 07:57:24.6412', '2026-05-13 07:57:24.6412', '10', '10', '10', NULL, NULL, 23, 9, 2.07, 23, 2.3, 90, 9);
INSERT INTO public.job_contract VALUES (13, 1, 10, 9, '2026-05-13', 'Padale Group', 'Siddhivinayak Textiles ', 'Ritesh Chougule', '62" 80*60 / 30+30slub * 30+30slub - Jacquard monogram', 6005, 15, '1.50 % CD = 20 days', 'Urgent ', 2, '2 by 2 Twill and Herringbone + Jacquard monogram ', '2026-05-05 14:58:13.514557', '2026-05-13 02:37:42.230297', '2+2', '-', '-', 'Folding', 'SIZING', 54045, 0, 540.45, 60, 9, 0, 1);
INSERT INTO public.job_contract VALUES (15, 1, 11, 10, '2026-05-13', 'Bhangadia Group ', 'Siddhivinayak Textiles ', 'Ritesh Chougule', '62" 80*60 / 30+30slub * 30+30slub - Jacquard monogram', 0, 14.75, '60', 'Urgent ', 3, 'Folding ', '2026-05-13 05:44:27.905816', '2026-05-13 05:44:27.905832', '3+3', '-', '-', 'Folding', 'SIZING', 'NaN', 'NaN', 'NaN', 60, 8.85, 0, 1);
INSERT INTO public.job_contract VALUES (16, 1, 151, 11, '2026-05-12', 'Bhangadia Group ', 'Siddhivinayak Textiles ', 'Ritesh Chougule', '62" 80*60 / 30+30slub * 30+30slub - Jacquard monogram', 30000, 13.5, '1.50 CD = 20days ', 'Regular', 3, 'Rolling', '2026-05-13 05:48:34.876785', '2026-05-13 05:48:34.876802', '6', '-', '-', 'Rolling', 'SIZING', 243000, 0, 2430, 60, 8.1, 0, 1);
INSERT INTO public.job_contract VALUES (17, 1, 152, 12, '2026-05-13', 'Bhangadia Group ', 'Siddhivinayak Textiles ', 'Ritesh Chougule', '62" 80*60 / 30+30slub * 30+30slub - Jacquard monogram', 30000, 14, '60 days ', 'Regular', 3, '2/2 Twill + Jacquard monogram ', '2026-05-13 05:52:19.549683', '2026-05-13 05:52:19.549705', '2+2', '-', '-', 'Folding', 'SIZING', 252000, 0, 2520, 60, 8.4, 0, 1);


--
-- TOC entry 3464 (class 0 OID 16523)
-- Dependencies: 234
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.payments VALUES (1, NULL, 99900, '2026-04-22 00:00:34.400369', 'akankshapawar8866@gmail.com', '9822842422', '2026-04-22 00:00:22.555125', 'APPROVED', '2026-04-21', '2026-04-22', 'Akanksha Pawar', '12345678990989833');
INSERT INTO public.payments VALUES (2, NULL, 500, '2026-04-22 19:22:44.849498', 'akankshapawar8866@gmail.com', '7499473363', '2026-04-22 19:21:32.673462', 'APPROVED', '2027-04-22', '2026-04-22', 'akanksha', '12345678990989097');
INSERT INTO public.payments VALUES (5, NULL, 500, '2026-04-22 22:37:23.856562', 'riteshchougule96@gmail.com', '7499473363', '2026-04-22 22:36:36.252013', 'APPROVED', '2027-04-22', '2026-04-22', 'Ritesh Chougule', '123456789558');
INSERT INTO public.payments VALUES (6, NULL, 1, '2026-04-25 10:08:36.912897', 'sheshnalwar@gmail.com', '7385395199', '2026-04-25 10:07:28.919755', 'APPROVED', '2027-04-25', '2026-04-25', 'shesh1', '121212121212');
INSERT INTO public.payments VALUES (7, NULL, 20, '2026-04-29 19:02:30.537438', 'siddharthdoshi53@gmail.com', '7028351408', '2026-04-29 19:02:06.91443', 'APPROVED', '2027-04-29', '2026-04-29', 'Siddharth Doshi', '702835140808');
INSERT INTO public.payments VALUES (8, NULL, 1000, '2026-05-13 12:15:16.470653', 'telsingemayur@gmail.com', '9766666905', '2026-05-13 12:14:55.833859', 'APPROVED', '2027-05-13', '2026-05-13', 'Mayur Telsinge', '2345678910');


--
-- TOC entry 3460 (class 0 OID 16494)
-- Dependencies: 230
-- Data for Name: qualitymaster; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.qualitymaster VALUES (2, '90', '90*90 / 90*90 / 90" / plain', '90', 'Plain', '90', '90', '2026-04-18 05:47:45.07973', 1, '90', NULL, NULL);
INSERT INTO public.qualitymaster VALUES (1, '23', '98*10 / 58 * 23 / 64" - Hard', '58', 'Hard', '10', '64', '2026-04-18 06:15:54.845074', 20, '98', '98*10 / 58 * 23 / 64" - Hard', '123');
INSERT INTO public.qualitymaster VALUES (4, '111', '111*450 / 56 * 111 / 67" - 111', '56', '111', '450', '67', '2026-04-22 23:44:16.552513', 1, '111', '111*450 / 56 * 111 / 67" - 111', '111');
INSERT INTO public.qualitymaster VALUES (6, '68', '40 PV*40PV / 84*68 / 62" / Plain', '84', 'Plain', '40PV', '62', '2026-04-25 04:45:34.078531', 1, '40 PV', NULL, NULL);
INSERT INTO public.qualitymaster VALUES (7, '70', '40pv*2/65+40pv / 88*70 / 62"" / Plain + Jacquard ', '88', 'Plain + Jacquard ', '2/65+40pv', '62"', '2026-04-25 05:32:54.388509', 1, '40pv', NULL, NULL);
INSERT INTO public.qualitymaster VALUES (8, '68', '33 karded compact *240 poly lycra  / 4/64*68 / 74" / 5/1', '4/64', '5/1', '240 poly lycra ', '74', '2026-04-27 10:12:01.063837', 1, '33 karded compact ', NULL, NULL);
INSERT INTO public.qualitymaster VALUES (16, '64', '40*40 / 66/2*64 / 65" / Plain', '66/2', 'Plain', '40', '65', '2026-05-03 09:18:43.351927', 1, '40', NULL, NULL);
INSERT INTO public.qualitymaster VALUES (17, '66', '65" 66/2*66 / 40*40 -Plain ', '66/2', 'Plain ', '40', '65', '2026-05-03 09:24:21.068931', 1, '40', '65" 66/2*66 / 40*40 -Plain ', '65');
INSERT INTO public.qualitymaster VALUES (18, '60', '65" 66/2*60 / 40 * 40 - Plain', '66/2', 'Plain', '40', '65', '2026-05-03 14:00:59.109332', 1, '40', NULL, '65');
INSERT INTO public.qualitymaster VALUES (19, '60', '62" 80*60 / 30+30slub * 30+30slub - Jacquard monogram', '80', 'Jacquard Monogram ', '30+30slub', '62', '2026-05-05 14:57:06.286012', 1, '30+30slub', NULL, '');
INSERT INTO public.qualitymaster VALUES (20, '72', '62" 120*72 / 80d. * 40 cotton  - Plain', '120', 'Plain', '40 cotton ', '62', '2026-05-13 12:19:35.951294', 6, '80d.', NULL, '');
INSERT INTO public.qualitymaster VALUES (21, '72', '62" 120*72 / 80d. * 40pc - Plain', '120', 'Plain', '40pc', '62', '2026-05-13 14:25:36.758834', 6, '80d.', NULL, '');
INSERT INTO public.qualitymaster VALUES (22, '90', '-" 4/64*90 / 50 cpt * 50cpt - Dobby as per paper', '4/64', 'Dobby As Per Paper ', '50cpt', '-', '2026-05-13 15:03:01.80147', 6, '50 cpt', NULL, 'RS 68"');
INSERT INTO public.qualitymaster VALUES (23, '90', 'RS 68" 4/64*90 / 50 cpt  * 50 cpt - Dobby as per paper', '4/64', 'Dobby As Per Paper ', '50 cpt', 'RS 68', '2026-05-13 15:06:29.801905', 6, '50 cpt ', NULL, '68');


--
-- TOC entry 3450 (class 0 OID 16415)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.users VALUES (9, 'ritesh@gmail.com', 'Ritesh', '$2a$10$SDg4Pk3V40gsSBWywnyape.FbeQ/iCIuqYy7pmf1HA0IzT3OJcv/K');
INSERT INTO public.users VALUES (10, 'v@gmail.com', 'Vivek', '$2a$10$/VhwZMU2sUiSn6u.5Lk0kOl4XKxEuAVpo2H8THtBg.4qjOwu7zgRi');
INSERT INTO public.users VALUES (11, 'chouguleriteshr@gmail.com', 'ritesh', '$2a$10$/VhwZMU2sUiSn6u.5Lk0kOl4XKxEuAVpo2H8THtBg.4qjOwu7zgRi');
INSERT INTO public.users VALUES (21, 'shekharpatil4545@gmail.com', 'shekhar', '$2a$10$PAGP3twdykDte7ebzMaJR.43mItaIO1kiygljm.7QgWiqb4NKAn3G');
INSERT INTO public.users VALUES (22, 'akanksha.pawar@smartrecontech.com', 'AkankshaPawar', '$2a$10$KVdUV0n7VWb9tU/pjnhHbOPeTAVQpjnzfmV3EXVwPwjRYFWlLUEfi');
INSERT INTO public.users VALUES (23, 'kopardeshritej111@gmail.com', 'Shreetej C koparde', '$2a$10$ItJxuM6v/PeDGCdidPAWp.wtHm69.CTBH1SVKT./NkVeq72x20J0O');
INSERT INTO public.users VALUES (20, 'akankshapawar8866@gmail.com', 'akanksha', '$2a$10$q5u7fgFsF8lFRRFc1P/8X.ZU92/0jE8B7mvbdHHUazIVzJsfBCpJ6');
INSERT INTO public.users VALUES (3, 'chetanhingmire1819@gmail.com', 'Chetan Hingmire', '$2a$10$TkS545Q6JpbwlZgDklqdC.2iBuiK49gimCNIPLUBbVfh6CpKGXf72');
INSERT INTO public.users VALUES (12, 'chougulera999@gmail.com', 'ramprakash', '$2a$10$UkuNYBn49AbzsKHKc3pbw.9by3Hu8aX.Q3wRF1zEnRDfx36ACtJVC');
INSERT INTO public.users VALUES (15, 'sheshnalwar@gmail.com', 'shesh1', '$2a$10$0.sgAspJlk082XFUEAPakOU8.TGvIa3pvW03Ed2hYkaZkRaOf8SUq');
INSERT INTO public.users VALUES (2, 'ritesh.chougule@smartrecontech.com', 'Ritesh Chougule', '$2a$10$ecYp1w0YCX1hEoLgHANKAOAyCCgmIfUKTlpJI9GOcWegjy7opOzoi');
INSERT INTO public.users VALUES (4, 'siddharthdoshi53@gmail.com', 'Siddharth Doshi', '$2a$10$EIvvUgCvyolJi5nNT.R0P.M0IK6rvDIO8ovKSv9HotmkeESu/OYhi');
INSERT INTO public.users VALUES (18, 'koparde.shritej111@gmail.com', 'shritej', '$2a$10$N09aNCVCfiduPo5lzkj8seRkW9.ln15kl8bZWOn47cJ4LaGIOkCh.');
INSERT INTO public.users VALUES (1, 'riteshchougule96@gmail.com', 'Ritesh Chougule', '$2a$10$MfQF8AZHmF3OaKw2HlRgNeXHOcm1lHPnou.T6ANjkX3HfwgA5a6Uu');
INSERT INTO public.users VALUES (5, 'eglxh97183@minitts.net', 'qs', '$2a$10$K9ICXuVqTf66dMXrr2Qy2OURmeIXixtuuSPhwM.8uZeXkK3dtCnea');
INSERT INTO public.users VALUES (6, 'telsingemayur@gmail.com', 'Mayur Telsinge', '$2a$10$F/LFQlMzscr2eAHFurNWLucp2FQnj/2FiihcB/DWJ69TE9z.ZPjf6');


--
-- TOC entry 3456 (class 0 OID 16461)
-- Dependencies: 226
-- Data for Name: wallet; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.wallet VALUES (3, 0, '2026-04-18 15:45:29.53824', '2026-04-18 15:45:29.538266', 2);
INSERT INTO public.wallet VALUES (4, 0, '2026-04-22 19:44:27.028177', '2026-04-22 19:44:27.028205', 3);
INSERT INTO public.wallet VALUES (5, 1, '2026-04-25 10:08:36.933247', '2026-04-25 10:08:36.947137', 15);
INSERT INTO public.wallet VALUES (6, 20, '2026-04-29 19:01:18.746446', '2026-04-29 19:02:30.641242', 4);
INSERT INTO public.wallet VALUES (7, 0, '2026-05-12 16:27:22.337505', '2026-05-12 16:27:22.337505', 5);
INSERT INTO public.wallet VALUES (1, 100850, '2026-04-17 23:16:22.061794', '2026-05-13 07:57:24.988385', 20);
INSERT INTO public.wallet VALUES (2, 1060, '2026-04-18 05:40:40.508219', '2026-05-13 06:29:43.329203', 1);
INSERT INTO public.wallet VALUES (8, 970, '2026-05-13 12:13:00.348363', '2026-05-13 15:04:25.774143', 6);


--
-- TOC entry 3458 (class 0 OID 16475)
-- Dependencies: 228
-- Data for Name: wallet_transactions; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.wallet_transactions VALUES (1, 500, '2026-04-17 23:16:22.275539', 'Payment approved for UTR: 12345678990989812', 500, 'DEPOSIT', 20, 1);
INSERT INTO public.wallet_transactions VALUES (2, -10, '2026-04-17 23:18:46.962592', 'Contract Creation: 1', 490, 'DEDUCTION', 20, 1);
INSERT INTO public.wallet_transactions VALUES (3, -10, '2026-04-17 23:22:26.24648', 'Contract Creation: 2', 480, 'DEDUCTION', 20, 1);
INSERT INTO public.wallet_transactions VALUES (4, 500, '2026-04-18 05:42:11.712186', 'Payment approved for UTR: 12345678990989987', 500, 'DEPOSIT', 1, 2);
INSERT INTO public.wallet_transactions VALUES (5, -10, '2026-04-18 05:49:35.612742', 'Contract Creation: 1', 490, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (6, 200, '2026-04-18 05:59:44.714755', 'Payment approved for UTR: 123456789123', 690, 'DEPOSIT', 1, 2);
INSERT INTO public.wallet_transactions VALUES (7, -10, '2026-04-18 07:15:36.892826', 'Contract Creation: 2', 680, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (8, -10, '2026-04-18 07:26:45.629341', 'Contract Creation: 3', 670, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (9, 99900, '2026-04-22 00:00:34.714632', 'Payment approved for UTR: 12345678990989833', 100380, 'DEPOSIT', 20, 1);
INSERT INTO public.wallet_transactions VALUES (10, 500, '2026-04-22 19:22:45.152698', 'Payment approved for UTR: 12345678990989097', 100880, 'DEPOSIT', 20, 1);
INSERT INTO public.wallet_transactions VALUES (11, 500, '2026-04-22 22:37:24.154855', 'Payment approved for UTR: 123456789558', 1170, 'DEPOSIT', 1, 2);
INSERT INTO public.wallet_transactions VALUES (12, -10, '2026-04-24 18:32:41.735824', 'Contract Creation: 4', 1160, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (13, -10, '2026-04-25 05:25:43.058132', 'Contract Creation: 5', 1150, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (14, 1, '2026-04-25 10:08:36.944479', 'Payment approved for UTR: 121212121212', 1, 'DEPOSIT', 15, 5);
INSERT INTO public.wallet_transactions VALUES (15, -10, '2026-04-29 20:07:05.142963', 'Contract Creation: 3', 100870, 'DEDUCTION', 20, 1);
INSERT INTO public.wallet_transactions VALUES (16, -10, '2026-04-29 20:20:23.709514', 'Contract Creation: 4', 100860, 'DEDUCTION', 20, 1);
INSERT INTO public.wallet_transactions VALUES (17, 20, '2026-04-29 19:02:30.635852', 'Payment approved for UTR: 702835140808', 20, 'DEPOSIT', 4, 6);
INSERT INTO public.wallet_transactions VALUES (18, -10, '2026-05-02 10:26:59.000957', 'Contract Creation: 7', 1140, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (19, -10, '2026-05-02 10:31:03.516571', 'Contract Creation: 8', 1130, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (20, -10, '2026-05-03 14:03:53.158733', 'Contract Creation: 9', 1120, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (21, -10, '2026-05-05 14:58:13.526033', 'Contract Creation: 10', 1110, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (22, -10, '2026-05-13 07:57:24.905219', 'Contract Creation: 5', 100850, 'DEDUCTION', 20, 1);
INSERT INTO public.wallet_transactions VALUES (23, -10, '2026-05-13 05:44:27.927898', 'Contract Creation: 11', 1100, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (24, -10, '2026-05-13 05:48:34.889866', 'Contract Creation: 151', 1090, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (25, -10, '2026-05-13 05:52:19.574903', 'Contract Creation: 152', 1080, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (26, -10, '2026-05-13 06:06:35.782876', 'Contract Creation: 154', 1070, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (27, -10, '2026-05-13 06:29:43.324689', 'Contract Creation: 156', 1060, 'DEDUCTION', 1, 2);
INSERT INTO public.wallet_transactions VALUES (28, 1000, '2026-05-13 12:15:16.485075', 'Payment approved for UTR: 2345678910', 1000, 'DEPOSIT', 6, 8);
INSERT INTO public.wallet_transactions VALUES (29, -10, '2026-05-13 12:20:14.131576', 'Contract Creation: 201', 990, 'DEDUCTION', 6, 8);
INSERT INTO public.wallet_transactions VALUES (30, -10, '2026-05-13 14:26:23.219465', 'Contract Creation: 202', 980, 'DEDUCTION', 6, 8);
INSERT INTO public.wallet_transactions VALUES (31, -10, '2026-05-13 15:04:25.771499', 'Contract Creation: 203', 970, 'DEDUCTION', 6, 8);


--
-- TOC entry 3462 (class 0 OID 16503)
-- Dependencies: 232
-- Data for Name: weaver_trader; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.weaver_trader VALUES (1, '2026-04-17 23:17:49.81055', 'Ritesh', 'WEAVER', 20, 7499473363, NULL, NULL, 90, 9);
INSERT INTO public.weaver_trader VALUES (2, '2026-04-17 23:18:12.300433', 'Chougule', 'TRADER', 20, 987654321, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (4, '2026-04-18 06:01:38.237521', 'Chougule', 'TRADER', 1, 7776070635, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (3, '2026-04-19 10:08:15.639019', 'Ritesh', 'WEAVER', 1, 7499473363, 9325008581, 9096661773, 0.22, 0.27);
INSERT INTO public.weaver_trader VALUES (5, '2026-04-25 04:43:54.293153', 'Onkar Mete ', 'WEAVER', 1, 9881309099, NULL, NULL, 0, 1);
INSERT INTO public.weaver_trader VALUES (6, '2026-04-25 04:44:56.387178', 'Pm Vastra Udyog ', 'TRADER', 1, 8856017601, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (7, '2026-04-25 16:26:47.338853', 'Onkar Mete ', 'WEAVER', 1, 9881309099, NULL, NULL, 0, 1);
INSERT INTO public.weaver_trader VALUES (9, '2026-04-27 10:10:52.626439', 'Bhangadia Group ', 'WEAVER', 1, 8788496567, NULL, NULL, 0, 1);
INSERT INTO public.weaver_trader VALUES (10, '2026-04-27 10:11:14.129825', 'Pankaj  Loya', 'TRADER', 1, 9420335735, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (12, '2026-05-05 14:55:27.859358', 'Padale Group', 'WEAVER', 1, 8421774112, NULL, NULL, 0, 1);
INSERT INTO public.weaver_trader VALUES (13, '2026-05-05 14:56:18.039657', 'Siddhivinayak Textiles ', 'TRADER', 1, 9423269785, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (15, '2026-05-13 12:18:23.459024', 'Rohit Karva', 'TRADER', 6, 9172658975, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (16, '2026-05-13 12:19:01.571367', 'Rohitji Karva', 'TRADER', 6, 9172658975, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (14, '2026-05-13 13:20:39.592808', 'Fine Fab, Akhsayji Rathi', 'WEAVER', 6, 7507566033, NULL, 9978989881, 0, 1);
INSERT INTO public.weaver_trader VALUES (17, '2026-05-13 14:24:15.410759', 'Sachinji Nimankar', 'WEAVER', 6, 8007621621, NULL, NULL, 0, 1);
INSERT INTO public.weaver_trader VALUES (18, '2026-05-13 14:25:08.00136', 'Pm Vastra Udyog ', 'TRADER', 6, 8856017601, NULL, NULL, 0, 0);
INSERT INTO public.weaver_trader VALUES (19, '2026-05-13 14:59:48.899665', 'Kapilji Kharge', 'WEAVER', 6, 9011675858, NULL, NULL, 0, 1);
INSERT INTO public.weaver_trader VALUES (20, '2026-05-13 15:01:23.0504', 'Mundra N Mundra ', 'TRADER', 6, 8855849454, NULL, NULL, 0, 0);


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 217
-- Name: admins_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.admins_id_seq', 1, false);


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 221
-- Name: email_otp_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.email_otp_id_seq', 33, true);


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 223
-- Name: job_contract_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.job_contract_id_seq', 22, true);


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 233
-- Name: payments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.payments_id_seq', 8, true);


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 229
-- Name: qualitymaster_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.qualitymaster_id_seq', 23, true);


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.users_id_seq', 6, true);


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 225
-- Name: wallet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.wallet_id_seq', 8, true);


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 227
-- Name: wallet_transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.wallet_transactions_id_seq', 31, true);


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 231
-- Name: weaver_trader_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.weaver_trader_id_seq', 20, true);


--
-- TOC entry 3270 (class 2606 OID 16411)
-- Name: admins admins_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT admins_pkey PRIMARY KEY (id);


--
-- TOC entry 3272 (class 2606 OID 16413)
-- Name: admins admins_username_key; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT admins_username_key UNIQUE (username);


--
-- TOC entry 3282 (class 2606 OID 16433)
-- Name: email_otp email_otp_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.email_otp
    ADD CONSTRAINT email_otp_pkey PRIMARY KEY (id);


--
-- TOC entry 3285 (class 2606 OID 16444)
-- Name: job_contract job_contract_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.job_contract
    ADD CONSTRAINT job_contract_pkey PRIMARY KEY (id);


--
-- TOC entry 3297 (class 2606 OID 16531)
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);


--
-- TOC entry 3293 (class 2606 OID 16501)
-- Name: qualitymaster qualitymaster_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qualitymaster
    ADD CONSTRAINT qualitymaster_pkey PRIMARY KEY (id);


--
-- TOC entry 3276 (class 2606 OID 16514)
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- TOC entry 3274 (class 2606 OID 16512)
-- Name: admins ukmi8vkhus4xbdbqcac2jm4spvd; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT ukmi8vkhus4xbdbqcac2jm4spvd UNIQUE (username);


--
-- TOC entry 3278 (class 2606 OID 16424)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3280 (class 2606 OID 16422)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3287 (class 2606 OID 16466)
-- Name: wallet wallet_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet
    ADD CONSTRAINT wallet_pkey PRIMARY KEY (id);


--
-- TOC entry 3291 (class 2606 OID 16482)
-- Name: wallet_transactions wallet_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet_transactions
    ADD CONSTRAINT wallet_transactions_pkey PRIMARY KEY (id);


--
-- TOC entry 3289 (class 2606 OID 16468)
-- Name: wallet wallet_user_id_key; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet
    ADD CONSTRAINT wallet_user_id_key UNIQUE (user_id);


--
-- TOC entry 3295 (class 2606 OID 16510)
-- Name: weaver_trader weaver_trader_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.weaver_trader
    ADD CONSTRAINT weaver_trader_pkey PRIMARY KEY (id);


--
-- TOC entry 3283 (class 1259 OID 16547)
-- Name: idx_user_contract; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_user_contract ON public.job_contract USING btree (user_id, contract_no);


--
-- TOC entry 3298 (class 2606 OID 16445)
-- Name: job_contract fk_job_contract_user; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.job_contract
    ADD CONSTRAINT fk_job_contract_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 3300 (class 2606 OID 16483)
-- Name: wallet_transactions wallet_transactions_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet_transactions
    ADD CONSTRAINT wallet_transactions_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3301 (class 2606 OID 16488)
-- Name: wallet_transactions wallet_transactions_wallet_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet_transactions
    ADD CONSTRAINT wallet_transactions_wallet_id_fkey FOREIGN KEY (wallet_id) REFERENCES public.wallet(id);


--
-- TOC entry 3299 (class 2606 OID 16469)
-- Name: wallet wallet_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.wallet
    ADD CONSTRAINT wallet_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2026-05-13 23:17:54

--
-- PostgreSQL database dump complete
--

\unrestrict BeWfRu5MIc9FeNgWExG66nfPFK8LzDRuJzUQ3QFm7N3CAgkvE98hdwaYkf7hg1A

