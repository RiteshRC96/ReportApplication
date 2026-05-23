--
-- PostgreSQL database dump
--

\restrict AAoF1EqykyRl5GIahwTrPV5ycJKdWas8R9kYprUt7nQRgdTgwqWS9lDnMNZodoe

-- Dumped from database version 17.9 (Debian 17.9-1.pgdg12+1)
-- Dumped by pg_dump version 18.0

-- Started on 2026-05-22 21:11:03

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
-- TOC entry 3447 (class 0 OID 16405)
-- Dependencies: 217
-- Data for Name: admins; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.admins (id, name, password, username) FROM stdin;
1	Administrator	$2a$10$LqxlBzmA/wXhCUlBzwfRkutk9j.ywYsMEKSX3F9idKhyHtZx9/Xnu	admin
\.


--
-- TOC entry 3449 (class 0 OID 16409)
-- Dependencies: 219
-- Data for Name: email_otp; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.email_otp (id, email, expiry_time, otp, used) FROM stdin;
33	telsingemayur@gmail.com	2026-05-13 12:16:54.250548	814108	t
34	demouser@gmail.com	2026-05-17 18:30:45.19646	894326	t
\.


--
-- TOC entry 3451 (class 0 OID 16415)
-- Dependencies: 221
-- Data for Name: job_contract; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.job_contract (id, user_id, contract_no, sr_no, contract_date, weaver_name, trader_name, broker_name, quality, quantity_meters, job_rate, payment_days, production_schedule, no_of_machines, remark, created_at, updated_at, beams, cut_length, minimum_delivery, rolling_folding, sizing_fabric, amount, brokerage_mtr_amt, brokerage_percent_amt, pick, rate, weaver_brokerage_paisa, weaver_brokerage_percent) FROM stdin;
20	6	201	1	2026-05-13	Fine Fab, Akhsayji Rathi	Rohitji Karva	Mayur Telsinge	62" 120*72 / 80d. * 40 cotton  - Plain	25000	11.5	60 days	Urgent 	3	Rolling	2026-05-13 12:20:14.120336	2026-05-13 12:20:14.120342	3	-	-	Rolling	SIZING	207000	0	2070	72	8.28	0	1
21	6	202	2	2026-05-13	Sachinji Nimankar	Pm Vastra Udyog 	Mayur Telsinge	62" 120*72 / 80d. * 40pc - Plain	25000	11.5	CD = 20 days	Urgent 	3	Rolling	2026-05-13 14:26:23.209329	2026-05-13 14:26:23.209336	3	-	-	Rolling	SIZING	207000	0	2070	72	8.28	0	1
22	6	203	3	2026-05-13	Kapilji Kharge	Mundra N Mundra 	Mayur Telsinge	RS 68" 4/64*90 / 50 cpt  * 50 cpt - Dobby as per paper	40000	12.75	60 days	Urgent 	4	Dobby as per paper 	2026-05-13 15:04:25.762375	2026-05-13 15:06:49.057877	4+4	-	-	Rolling	SIZING	459000	0	4590	90	11.47	0	1
23	6	204	4	2026-05-14	Padale Group	Nisar Textile 	Mayur Telsinge	62" 88*72 / 40pv * 2/65d. - Plain + Jacquard	36000	13	50-55 days	Urgent 	6	Folding 	2026-05-14 07:01:15.512872	2026-05-14 07:41:05.026007	6	-	-	Folding	SIZING	336960	0	3369.6	72	9.36	0	1
26	6	207	7	2026-05-15	Ashishji Ganjve	Arvind Textiles 	Mayur Telsinge	62" 76/2*54 / 20pc * 150d. - Twill	50000	12.25	1.50 % on 8th of month	Regular	3	Folding 	2026-05-15 11:17:54.328866	2026-05-15 11:17:54.32887	-	-	-	Folding	SIZING	330750	5000	0	54	6.62	10	0
24	6	205	5	2026-05-14	Rohitji Mete 	Pramodji Sangave	Mayur Telsinge	RS 69" 88*46 / 40 * (60 +16 encounter) - Plain	15000	12	50-55 days	Regular	1	Folding 	2026-05-14 14:34:18.676593	2026-05-14 14:34:18.676596	3	-	-	Folding	SIZING	82800	1500	0	46	5.52	10	0
25	6	206	6	2026-05-14	Rohitji Mete 	Nisar Textile 	Mayur Telsinge	LV 62" 84*50 / 40pv * (20psf+150d.) - Plain	36000	11	60 days	20 May	3	Folding 	2026-05-14 15:40:10.46425	2026-05-14 15:40:10.464253	3+3	-	-	Folding	SIZING	198000	3600	0	50	5.5	10	0
27	6	208	8	2026-05-15	Amitji Sarda	Rohitji Mete 	Mayur Telsinge	RS 67" 92*74 / 40pv * 2/65d. - Plain with Dobby Monogram	16000	13	60 days	Regular	1	Folding 	2026-05-15 12:43:35.549492	2026-05-15 12:43:35.549496	-	-	-	Folding	SIZING	153920	0	1539.2	74	9.62	0	1
28	6	209	9	2026-05-16	Akshayji Dake 	Shree Sai Tex	Mayur Telsinge	62" 108*52 / 150d. * 20cp - Drill	50000	11.25	CD payment 	Knotting 	2	Folding 	2026-05-16 07:57:03.706804	2026-05-16 07:57:03.706807	-	-	-	Folding	SIZING	292500	0	2925	52	5.85	0	1
29	6	210	10	2026-05-16	Akshayji Dake 	Shree Sai Tex	Mayur Telsinge	62" 84*36 / 150 * 20cp - Drill	50000	12	CD payment 	Urgent 	2	Folding 	2026-05-16 07:59:26.05166	2026-05-16 07:59:26.051664	-	-	-	Folding	SIZING	216000	0	2160	36	4.32	0	1
30	6	211	11	2026-05-17	Akashji Kadam	Nisar Textile 	Mayur Telsinge	LV 62" 84*50 / 40pv * (20psf+150d.) - Plain	48000	11	60 days	Knotting 	4	Folding 	2026-05-17 05:38:20.775232	2026-05-17 05:38:20.775256	4+4	-	-	Folding	\N	264000	0	2640	50	5.5	0	1
31	6	212	12	2026-05-17	Sarveshji Chaparwal 	Nisar Textile 	Mayur Telsinge	LV 38"+38" 83*50 / 40pv * (20psf+150d.) - Plain	24000	12.25	60 days 	Regular	2	Folding / 83 reed	2026-05-17 05:42:06.312709	2026-05-17 05:42:06.312726	2+2	-	-	Folding	SIZING	147000	0	1470	50	6.13	0	1
32	6	213	13	2026-05-18	Nitinji Redekar	Rajuji Dargad	Mayur Telsinge	62" 84*72 / 36hm * 36hm - Plain + Jacquard	14000	12	60 days	Urgent 	2	Folding 	2026-05-18 06:37:04.352643	2026-05-18 06:37:04.352658	3	-	-	Folding	SIZING	120960	1400	0	72	8.64	10	0
33	6	214	14	2026-05-18	Ashishji Ganjve	Vikramji Nahata 	Mayur Telsinge	62" 80*46 / 50 * 20 - Plain	27000	12.25	1.50 % cd on 8th of month	Regular	2	Rolling	2026-05-18 14:33:12.510875	2026-05-18 14:33:12.510892	2+2	-	-	Rolling	SIZING	152145	2700	0	46	5.63	10	0
34	6	215	15	2026-05-19	Kapilji Kharge	Arvind Textiles 	Mayur Telsinge	62" 60*54 / 33pc * 32psf - Plain	58000	10	60 days	10 days	3	Folding 	2026-05-19 05:19:13.058818	2026-05-19 05:19:13.058827	3+3+3	-	-	Folding	SIZING	313200	0	3132	54	5.4	0	1
35	6	216	16	2026-05-19	Fine Fab, Akhsayji Rathi	Pm Vastra Udyog 	Mayur Telsinge	62" 96*44 / 2/80d. * 2/30 psf  - Oxford	60000	11.25	60 days	Regular	3	Rolling	2026-05-19 06:03:36.48044	2026-05-19 06:03:36.480459	-	-	-	Rolling	SIZING	297000	0	2970	44	4.95	0	1
36	6	217	17	2026-05-19	Akshayji Dake 	Sagarji Lakhotia 	Mayur Telsinge	RS 68" 108*48 / 40 * 20 - Oxford	18000	10.5	CD payment 	Urgent 	2	Rolling	2026-05-19 06:54:06.967498	2026-05-19 06:54:06.967501	-	-	-	Rolling	SIZING	90720	0	907.2	48	5.04	0	1
37	6	218	18	2026-05-19	Nitinji Mantri	Anita Texcom 	Mayur Telsinge	54" 84*80 / 60 * 60 - Plain	28000	8	60 days	Urgent 	3	Rolling	2026-05-19 12:56:36.426956	2026-05-19 12:56:36.42696	3	-	-	Rolling	SIZING	179200	0	1792	80	6.4	0	1
38	6	219	19	2026-05-19	Ukarde Group	Pm Vastra Udyog 	Mayur Telsinge	62" 120*72 / 80d. * 40pc - Plain	30000	10.5	60 days	10-12 days 	3	Rolling	2026-05-19 13:25:52.143781	2026-05-19 13:25:52.143784	3	-	-	Rolling	SIZING	226800	0	2268	72	7.56	0	1
39	6	220	20	2026-05-20	Akshayji Dake 	Shree Sai Tex	Mayur Telsinge	62" 108*48 / 150d. * 20cp  - Twill	50000	11.25	CD payment 	Urgent 	2	Folding 	2026-05-20 13:44:47.245863	2026-05-20 13:44:47.245865	-	-	-	Folding	SIZING	270000	0	2700	48	5.4	0	1
40	6	221	21	2026-05-21	Akashji Kadam	Nisar Textile 	Mayur Telsinge	LV 38"+38" 84*50 / 40pv * (20psf+150d.) - Plain	48000	12	60 days	Knotting 	4	Folding 	2026-05-21 08:23:14.947654	2026-05-21 08:23:14.947659	4+4	-	-	Folding	SIZING	288000	0	2880	50	6	0	1
41	6	222	22	2026-05-22	Vinayakji Nikam 	Shree Sai Tex	Mayur Telsinge	62" 108*68 / 60cpt  * 50cpt - Plain	36000	8	60 days 	25 May 	3	Rolling	2026-05-22 05:02:11.931397	2026-05-22 05:02:11.9314	3+3	-	-	Rolling	SIZING	195840	0	1958.4	68	5.44	0	1
42	6	223	23	2026-05-22	Madhurji Rathi 	Mundra N Mundra 	Mayur Telsinge	62" 88*72 / 40pv * 2/65d. - Plain + Jacquard	20000	12	60 days	7 days 	2	Folding 	2026-05-22 07:26:50.699214	2026-05-22 07:26:50.699217	3	-	-	Folding	SIZING	172800	0	1728	72	8.64	0	1
43	6	224	24	2026-05-22	Ashishji Bhoje	Shree Sai Tex	Mayur Telsinge	62" 108*68 / 60cpt  * 50cpt - Plain	36000	8	60 days	Urgent 	3	Rolling	2026-05-22 10:40:26.091078	2026-05-22 10:40:26.091081	3+3	-	-	Rolling	SIZING	195840	0	1958.4	68	5.44	0	1
44	6	225	25	2026-05-22	Ashishji Ganjve	Pm Vastra Udyog 	Mayur Telsinge	62" 84*72 / 40psf  * 40psf - Plain	40000	10.5	CD payment 	8-10 days	3	Folding 	2026-05-22 11:04:26.857688	2026-05-22 11:04:26.85769	-	-	-	Folding	SIZING	302400	4000	0	72	7.56	10	0
45	7	1	1	2026-05-22	Prasad Koshti	Sarojlaxmi Texcom	Demo User	62" 47*40 / 60 * 80 - PLAIN	24139	60	15	30 Days	0	Delivery in next 60 Days	2026-05-22 13:58:00.493391	2026-05-22 14:01:19.138191	0	73.15	22 Taga chi 1 bale	Folding	FABRIC	579336	0	0	40	24	0	0
\.


--
-- TOC entry 3453 (class 0 OID 16423)
-- Dependencies: 223
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.payments (id, admin_notes, amount, approval_date, email, mobile_number, payment_date, status, subscription_end_date, subscription_start_date, user_name, utr_number) FROM stdin;
8	\N	1000	2026-05-13 12:15:16.470653	telsingemayur@gmail.com	9766666905	2026-05-13 12:14:55.833859	APPROVED	2027-05-13	2026-05-13	Mayur Telsinge	2345678910
9	\N	99	2026-05-17 18:28:07.652488	demouser@gmail.com	7499473363	2026-05-17 18:26:49.495574	APPROVED	2027-05-17	2026-05-17	Demo User	234234234234
\.


--
-- TOC entry 3455 (class 0 OID 16430)
-- Dependencies: 225
-- Data for Name: qualitymaster; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.qualitymaster (id, pick, qulity_name, reed, weave, weft, width, created_at, user_id, warp, alias, reed_space) FROM stdin;
20	72	62" 120*72 / 80d. * 40 cotton  - Plain	120	Plain	40 cotton 	62	2026-05-13 12:19:35.951294	6	80d.	\N	
21	72	62" 120*72 / 80d. * 40pc - Plain	120	Plain	40pc	62	2026-05-13 14:25:36.758834	6	80d.	\N	
22	90	-" 4/64*90 / 50 cpt * 50cpt - Dobby as per paper	4/64	Dobby As Per Paper 	50cpt	-	2026-05-13 15:03:01.80147	6	50 cpt	\N	RS 68"
23	90	RS 68" 4/64*90 / 50 cpt  * 50 cpt - Dobby as per paper	4/64	Dobby As Per Paper 	50 cpt	RS 68	2026-05-13 15:06:29.801905	6	50 cpt 	\N	68
24	72	62" 88*72 / 40pv * 2/65d. - Plain + Jacquard	88	Plain + Jacquard 	2/65d.	62	2026-05-14 07:00:31.556635	6	40pv	\N	
25	46	RS 69" 88*46 / 40 * (60 +16 encounter) - Plain	88	Plain	(60 +16 encounter)	RS 69	2026-05-14 14:33:34.412162	6	40	\N	
26	50	LV 62" 84*50 / 40pv * (20psf+150d.) - Plain	84	Plain	(20psf+150d.)	LV 62	2026-05-14 15:39:05.589878	6	40pv	\N	
27	54	62" 76/2*54 / 20pc * 150d. - Twill	76/2	Twill	150d.	62	2026-05-15 11:16:37.590934	6	20pc	\N	
28	74	RS 67" 92*74 / 40pv * 2/65d. - Plain with Dobby Monogram	92	Plain With Dobby Monogram 	2/65d.	RS 67	2026-05-15 12:42:30.495282	6	40pv	\N	
29	52	62" 108*52 / 150d. * 20 - Drill	108	Drill	20	62	2026-05-16 07:55:46.025191	6	150d.	\N	
30	52	62" 108*52 / 150d. * 20cp - Drill	108	Drill	20cp	62	2026-05-16 07:56:17.940718	6	150d.	\N	
31	36	62" 84*36 / 150 * 20cp - Drill	84	Drill	20cp	62	2026-05-16 07:58:40.949367	6	150	\N	
32	50	LV 38"+38" 83*50 / 40pv * (20psf+150d.) - Plain	83	Plain	(20psf+150d.)	LV 38"+38	2026-05-17 05:41:20.696308	6	40pv	\N	
33	72	62" 84*72 / 36hm * 36hm - Plain + Jacquard	84	Plain + Jacquard 	36hm	62	2026-05-18 06:36:30.13521	6	36hm	\N	
34	46	62" 80*46 / 50 * 20 - Plain	80	Plain	20	62	2026-05-18 14:32:24.795918	6	50	\N	
35	54	62" 60*54 / 33pc * 32psf - Plain	60	Plain	32psf	62	2026-05-19 05:17:12.674841	6	33pc	\N	
36	44	62" 96*44 / 2/80d. * 2/30 psf  - Oxford	96	Oxford 	2/30 psf 	62	2026-05-19 06:02:55.940784	6	2/80d.	\N	
37	48	RS 68" 108*48 / 40 * 20 - Oxford	108	Oxford 	20	RS 68	2026-05-19 06:53:28.279891	6	40	\N	
38	80	54" 84*80 / 60 * 60 - Plain	84	Plain	60	54	2026-05-19 12:55:01.030559	6	60	\N	
39	40	62" 47*40 / 60 * 80 - PLAIN	47	PLAIN	80	62	2026-05-19 14:52:17.167259	7	60	62" 47*40 / 60 * 80 - PLAIN	66
40	48	62" 108*48 / 150d. * 20cp  - Twill	108	Twill	20cp 	62	2026-05-20 13:44:19.966207	6	150d.	\N	
41	50	LV 38"+38" 84*50 / 40pv * (20psf+150d.) - Plain	84	Plain	(20psf+150d.)	LV 38"+38	2026-05-21 08:22:41.089103	6	40pv	\N	
42	68	62" 108*68 / 60cpt  * 50cpt - Plain	108	Plain	50cpt	62	2026-05-22 05:01:30.991469	6	60cpt 	\N	
43	72	62" 84*72 / 40psf  * 40psf - Plain	84	Plain	40psf	62	2026-05-22 11:03:39.854548	6	40psf 	\N	
\.


--
-- TOC entry 3457 (class 0 OID 16436)
-- Dependencies: 227
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.users (id, email, name, password) FROM stdin;
7	demouser@gmail.com	Demo User	$2a$10$5xNkqTUYRSK1WFpnGiYQdOeIZD91ThNMV1GxkyQOC1Qih1kpCpMKy
6	telsingemayur@gmail.com	Mayur Telsinge	$2a$10$F/LFQlMzscr2eAHFurNWLucp2FQnj/2FiihcB/DWJ69TE9z.ZPjf6
\.


--
-- TOC entry 3459 (class 0 OID 16442)
-- Dependencies: 229
-- Data for Name: wallet; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.wallet (id, balance, created_at, updated_at, user_id) FROM stdin;
8	776	2026-05-13 12:13:00.348363	2026-05-22 11:04:26.872618	6
9	92	2026-05-17 18:26:22.297365	2026-05-22 13:58:00.507443	7
\.


--
-- TOC entry 3461 (class 0 OID 16446)
-- Dependencies: 231
-- Data for Name: wallet_transactions; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.wallet_transactions (id, amount, created_at, description, remaining_bal, transaction_type, user_id, wallet_id) FROM stdin;
28	1000	2026-05-13 12:15:16.485075	Payment approved for UTR: 2345678910	1000	DEPOSIT	6	8
29	-10	2026-05-13 12:20:14.131576	Contract Creation: 201	990	DEDUCTION	6	8
30	-10	2026-05-13 14:26:23.219465	Contract Creation: 202	980	DEDUCTION	6	8
31	-10	2026-05-13 15:04:25.771499	Contract Creation: 203	970	DEDUCTION	6	8
32	-10	2026-05-14 07:01:15.525982	Contract Creation: 204	960	DEDUCTION	6	8
33	-10	2026-05-14 14:34:18.686567	Contract Creation: 205	950	DEDUCTION	6	8
34	-10	2026-05-14 15:40:10.476875	Contract Creation: 206	940	DEDUCTION	6	8
35	-10	2026-05-15 11:17:54.337947	Contract Creation: 207	930	DEDUCTION	6	8
36	-10	2026-05-15 12:43:35.55815	Contract Creation: 208	920	DEDUCTION	6	8
37	-10	2026-05-16 07:57:03.721484	Contract Creation: 209	910	DEDUCTION	6	8
38	-10	2026-05-16 07:59:26.066865	Contract Creation: 210	900	DEDUCTION	6	8
39	-10	2026-05-17 05:38:20.795385	Contract Creation: 211	890	DEDUCTION	6	8
40	-10	2026-05-17 05:42:06.323217	Contract Creation: 212	880	DEDUCTION	6	8
41	99	2026-05-17 18:28:07.675906	Payment approved for UTR: 234234234234	99	DEPOSIT	7	9
42	-8	2026-05-18 06:37:04.362733	Contract Creation: 213	872	DEDUCTION	6	8
43	-8	2026-05-18 14:33:12.532973	Contract Creation: 214	864	DEDUCTION	6	8
44	-8	2026-05-19 05:19:13.071103	Contract Creation: 215	856	DEDUCTION	6	8
45	-8	2026-05-19 06:03:36.506291	Contract Creation: 216	848	DEDUCTION	6	8
46	-8	2026-05-19 06:54:06.979812	Contract Creation: 217	840	DEDUCTION	6	8
47	-8	2026-05-19 12:56:36.442494	Contract Creation: 218	832	DEDUCTION	6	8
48	-8	2026-05-19 13:25:52.15889	Contract Creation: 219	824	DEDUCTION	6	8
49	-8	2026-05-20 13:44:47.258331	Contract Creation: 220	816	DEDUCTION	6	8
50	-8	2026-05-21 08:23:14.989406	Contract Creation: 221	808	DEDUCTION	6	8
51	-8	2026-05-22 05:02:11.945416	Contract Creation: 222	800	DEDUCTION	6	8
52	-8	2026-05-22 07:26:50.712265	Contract Creation: 223	792	DEDUCTION	6	8
53	-8	2026-05-22 10:40:26.103856	Contract Creation: 224	784	DEDUCTION	6	8
54	-8	2026-05-22 11:04:26.868321	Contract Creation: 225	776	DEDUCTION	6	8
55	-8	2026-05-22 13:58:00.503137	Contract Creation: 1	92	DEDUCTION	7	9
\.


--
-- TOC entry 3463 (class 0 OID 16452)
-- Dependencies: 233
-- Data for Name: weaver_trader; Type: TABLE DATA; Schema: public; Owner: job_contract_gcc5_user
--

COPY public.weaver_trader (id, created_at, name, type, user_id, phno, divanji_mob_no, owner_mob_no, weaver_brokerage_paisa, weaver_brokerage_percent) FROM stdin;
15	2026-05-13 12:18:23.459024	Rohit Karva	TRADER	6	9172658975	\N	\N	0	0
16	2026-05-13 12:19:01.571367	Rohitji Karva	TRADER	6	9172658975	\N	\N	0	0
14	2026-05-13 13:20:39.592808	Fine Fab, Akhsayji Rathi	WEAVER	6	7507566033	\N	9978989881	0	1
17	2026-05-13 14:24:15.410759	Sachinji Nimankar	WEAVER	6	8007621621	\N	\N	0	1
18	2026-05-13 14:25:08.00136	Pm Vastra Udyog 	TRADER	6	8856017601	\N	\N	0	0
19	2026-05-13 14:59:48.899665	Kapilji Kharge	WEAVER	6	9011675858	\N	\N	0	1
20	2026-05-13 15:01:23.0504	Mundra N Mundra 	TRADER	6	8855849454	\N	\N	0	0
21	2026-05-14 06:59:10.584996	Padale Group	WEAVER	6	7709048910	8421774112	\N	\N	1
22	2026-05-14 06:59:51.849779	Nisar Textile 	TRADER	6	9850486005	9158479597	\N	\N	\N
23	2026-05-14 07:40:02.182594	Padale Group	WEAVER	6	7709048910	\N	\N	0	1
24	2026-05-14 07:40:54.524613	Nisar Textile 	TRADER	6	9850486005	\N	\N	0	0
25	2026-05-14 14:30:55.864854	Rohitji Mete 	WEAVER	6	8888812440	\N	\N	10	0
26	2026-05-14 14:31:26.888586	Pramodji Sangave	TRADER	6	9834663505	\N	\N	0	0
27	2026-05-15 11:15:18.758637	Ashishji Ganjve	WEAVER	6	9975415778	\N	\N	10	0
28	2026-05-15 11:15:59.853941	Arvind Textiles 	TRADER	6	7666443061	\N	\N	0	0
29	2026-05-15 12:40:03.109005	Amitji Sarda	WEAVER	6	9421100701	\N	\N	0	1
30	2026-05-15 12:40:51.361615	Amitji Sarda	WEAVER	6	9421100701	\N	\N	0	1
31	2026-05-15 12:41:30.615009	Rohitji Mete 	TRADER	6	8888812440	\N	\N	0	0
32	2026-05-16 07:54:12.308335	Akshayji Dake 	WEAVER	6	9623457255	\N	\N	0	1
33	2026-05-16 07:54:51.500268	Shree Sai Tex	TRADER	6	7020918689	\N	\N	0	0
34	2026-05-17 05:37:28.469919	Akashji Kadam	WEAVER	6	9133173555	\N	\N	0	1
35	2026-05-17 05:40:09.80107	Sarveshji Chaparwal 	WEAVER	6	7588552293	\N	\N	0	1
36	2026-05-18 06:33:57.929713	Nitinji Redekar	WEAVER	6	9922663240	\N	\N	10	0
37	2026-05-18 06:36:08.048921	Rajuji Dargad	TRADER	6	8668632446	\N	\N	0	0
38	2026-05-18 14:31:03.827011	Vikramji Nahata 	TRADER	6	9850927521	\N	\N	0	0
39	2026-05-19 06:52:26.305266	Sagarji Lakhotia 	TRADER	6	9403601030	\N	\N	0	0
40	2026-05-19 12:54:12.188581	Nitinji Mantri	WEAVER	6	9403467261	\N	\N	0	1
41	2026-05-19 12:54:39.414445	Anita Texcom 	TRADER	6	9420352691	\N	\N	0	0
42	2026-05-19 13:25:04.493516	Ukarde Group	WEAVER	6	9372882232	\N	\N	0	1
43	2026-05-19 14:48:51.320301	Sarojlaxmi Texcom	TRADER	7	9823428698	\N	\N	\N	\N
44	2026-05-19 14:49:30.49206	Ankit Ji Bajaj	TRADER	7	8446682232	\N	\N	\N	\N
46	2026-05-19 14:51:03.211524	Santosh Redekar	WEAVER	7	8435811453	\N	\N	\N	0.5
47	2026-05-22 05:01:00.054458	Vinayakji Nikam 	WEAVER	6	9922937694	\N	\N	0	1
48	2026-05-22 07:24:38.757524	Madhurji Rathi 	WEAVER	6	9022406968	\N	\N	0	1
49	2026-05-22 07:24:38.778845	Madhurji Rathi 	WEAVER	6	9022406968	\N	\N	0	1
50	2026-05-22 07:24:38.779426	Madhurji Rathi 	WEAVER	6	9022406968	\N	\N	0	1
51	2026-05-22 07:24:38.78047	Madhurji Rathi 	WEAVER	6	9022406968	\N	\N	0	1
52	2026-05-22 07:24:38.780574	Madhurji Rathi 	WEAVER	6	9022406968	\N	\N	0	1
53	2026-05-22 10:39:40.564369	Ashishji Bhoje	WEAVER	6	7301128989	\N	\N	0	1
45	2026-05-22 14:01:02.103052	Prasad Koshti	WEAVER	7	7020211326	\N	\N	0	1
\.


--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 218
-- Name: admins_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.admins_id_seq', 1, false);


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 220
-- Name: email_otp_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.email_otp_id_seq', 34, true);


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 222
-- Name: job_contract_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.job_contract_id_seq', 45, true);


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 224
-- Name: payments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.payments_id_seq', 9, true);


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 226
-- Name: qualitymaster_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.qualitymaster_id_seq', 43, true);


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 228
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.users_id_seq', 7, true);


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 230
-- Name: wallet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.wallet_id_seq', 9, true);


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 232
-- Name: wallet_transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.wallet_transactions_id_seq', 55, true);


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 234
-- Name: weaver_trader_id_seq; Type: SEQUENCE SET; Schema: public; Owner: job_contract_gcc5_user
--

SELECT pg_catalog.setval('public.weaver_trader_id_seq', 53, true);


-- Completed on 2026-05-22 21:11:17

--
-- PostgreSQL database dump complete
--

\unrestrict AAoF1EqykyRl5GIahwTrPV5ycJKdWas8R9kYprUt7nQRgdTgwqWS9lDnMNZodoe

