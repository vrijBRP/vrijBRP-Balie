create or replace view zaken_aantal 
   as (select 'uitt_aanvr'     as tabel,   10 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from uitt_aanvr, 
                               document where uitt_aanvr.c_document = document.c_document and document.type = 'pl'                  group by ind_verwerkt
union select 'geheimhouding'   as tabel,   20 as zaak_type, ind_verwerkt, count (distinct (zaak_id)) as aantal from geheimhouding   group by ind_verwerkt
union select 'naamgebruik'     as tabel,   30 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from naamgebruik                group by ind_verwerkt
union select 'bvh_park'        as tabel,   40 as zaak_type, ind_verwerkt, count (distinct (zaak_id)) as aantal from bvh_park        group by ind_verwerkt
union select 'vog_aanvr'       as tabel,   50 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from vog_aanvr                  group by ind_verwerkt
union select 'gpk'             as tabel,   60 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from gpk                        group by ind_verwerkt
union select 'rdm01'           as tabel,   70 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from rdm01                      group by ind_verwerkt
union select 'reisd_inh'       as tabel,   75 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from reisd_inh                  group by ind_verwerkt
union select 'nrd'             as tabel,   80 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from nrd                        group by ind_verwerkt
union select 'terugmelding'    as tabel,   90 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from terugmelding               group by ind_verwerkt
union select 'doss_geb'        as tabel,  100 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 100 group by ind_verwerkt
union select 'doss_erk'        as tabel,  200 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 200 group by ind_verwerkt
union select 'doss_huw'        as tabel,  300 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 300 group by ind_verwerkt
union select 'doss_naam'       as tabel,  400 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 400 group by ind_verwerkt
union select 'doss_overl_gem'  as tabel,  500 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 500 group by ind_verwerkt
union select 'doss_lijkv'      as tabel,  600 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 600 group by ind_verwerkt
union select 'doss_overl_bui'  as tabel,  700 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 700 group by ind_verwerkt
union select 'doss_levenloos'  as tabel,  800 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from doss where type_doss = 800 group by ind_verwerkt
union select 'indicatie     '  as tabel,  900 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from indicatie                  group by ind_verwerkt
union select 'correspondentie' as tabel, 1100 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from correspondentie            group by ind_verwerkt
union select 'gv'              as tabel, 1200 as zaak_type, ind_verwerkt, count (zaak_id) as aantal from gv                         group by ind_verwerkt
);