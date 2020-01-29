-- Table: public.urlset
-- DROP TABLE public.urlset;
CREATE TABLE public.urlset
(
  uri character varying(255) NOT NULL,
  lastmod character varying(255),
  loc character varying(255),
  CONSTRAINT urlset_pkey PRIMARY KEY (uri)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.urlset
  OWNER TO postgres;


-- Table: public.url
-- DROP TABLE public.url;
CREATE TABLE public.url
(
  loc character varying(255) NOT NULL,
  changefreq character varying(255),
  lastmod character varying(255),
  priority character varying(255),
  urlset_uri character varying(255),
  CONSTRAINT url_pkey PRIMARY KEY (loc),
  CONSTRAINT urlset_fkey FOREIGN KEY (urlset_uri)
      REFERENCES public.urlset (uri) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.url
  OWNER TO postgres;