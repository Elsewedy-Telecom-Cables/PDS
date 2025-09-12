IF DB_ID('pds') IS NULL EXECUTE('CREATE DATABASE [pds];');
GO

USE [pds];
GO

IF SCHEMA_ID('dbo') IS NULL EXECUTE('CREATE SCHEMA [dbo];');
GO

CREATE  TABLE pds.dbo.sections (
                                   section_id           int    IDENTITY  NOT NULL,
                                   section_name         varchar(100)      NOT NULL,
                                   CONSTRAINT pk_sections PRIMARY KEY CLUSTERED ( section_id  asc )
);
GO

CREATE  TABLE pds.dbo.stages (
                                 stage_id             int    IDENTITY  NOT NULL,
                                 stage_name           varchar(100)      NULL,
                                 CONSTRAINT pk_stages PRIMARY KEY CLUSTERED ( stage_id  asc )
);
GO

CREATE  TABLE pds.dbo.users (
                                user_id              int    IDENTITY  NOT NULL,
                                emp_code             int      NOT NULL,
                                user_name            varchar(100)      NOT NULL,
                                password             varchar(50)      NOT NULL,
                                full_name            nvarchar(100)      NULL,
                                phone                varchar(11)      NULL,
                                role                 int      NOT NULL,
                                active               int      NOT NULL,
                                creation_date        varchar(50)      NULL,
                                CONSTRAINT pk_users PRIMARY KEY CLUSTERED ( user_id  asc ) ,
                                CONSTRAINT unq_users_user_name UNIQUE ( user_name  asc )
);
GO

CREATE  TABLE pds.dbo.machines (
                                   machine_id           int    IDENTITY  NOT NULL,
                                   machine_name         nvarchar(100)      NOT NULL,
                                   stage_id             int      NULL,
                                   CONSTRAINT pk_machines PRIMARY KEY CLUSTERED ( machine_id  asc )
);
GO

CREATE  TABLE pds.dbo.premises (
                                   premises_id          int    IDENTITY  NOT NULL,
                                   stage_description    nvarchar(255)      NOT NULL,
                                   user_id              int      NULL,
                                   machine_id           int      NOT NULL,
                                   theoritical_line_speed decimal(10,5)      NULL,
                                   tip_size             decimal(10,5)      NULL,
                                   tip_type             nvarchar(50)      NULL,
                                   die_size             decimal(10,5)      NULL,
                                   die_type             nvarchar(50)      NULL,
                                   path_size            decimal(10,5)      NULL,
                                   path_type            nvarchar(50)      NULL,
                                   fiber_tension        decimal(10,5)      NULL,
                                   number_of_fibers     decimal(10,5)      NULL,
                                   pay_off_1_motor_load decimal(10,5)      NULL,
                                   pay_off_1_dancer_tension decimal(10,5)      NULL,
                                   pay_off_2_motor_load decimal(10,5)      NULL,
                                   pay_off_2_dancer_tension decimal(10,5)      NULL,
                                   pay_off_3_motor_load decimal(10,5)      NULL,
                                   pay_off_3_dancer_tension decimal(10,5)      NULL,
                                   take_up_dancer_tension decimal(10,5)      NULL,
                                   take_up__motor_load  decimal(10,5)      NULL,
                                   take_up__traverse_gap decimal(10,5)      NULL,
                                   trough_1_temp        decimal(10,5)      NULL,
                                   notes                nvarchar(300)      NULL,
                                   CONSTRAINT pk_premises PRIMARY KEY CLUSTERED ( premises_id  asc )
);
GO

CREATE  TABLE pds.dbo.rewinding (
                                    rewinding_id         int    IDENTITY  NOT NULL,
                                    stage_description    nvarchar(255)      NOT NULL,
                                    machine_id           int      NOT NULL,
                                    user_id              int      NULL,
                                    speed                decimal(10,5)      NULL,
                                    notes                nvarchar(300)      NULL,
                                    CONSTRAINT pk_rewinding PRIMARY KEY CLUSTERED ( rewinding_id  asc )
);
GO

CREATE  TABLE pds.dbo.sections_machines (
                                            section_id           int      NULL,
                                            machine_id           int      NULL
);
GO

CREATE  TABLE pds.dbo.sheathing_data (
                                         sheathing_data_id    int    IDENTITY  NOT NULL,
                                         stage_description    nvarchar(255)      NOT NULL,
                                         machine_id           int      NOT NULL,
                                         user_id              int      NULL,
                                         feeding_1            decimal(10,5)      NULL,
                                         z_1                  decimal(10,5)      NULL,
                                         z_2                  decimal(10,5)      NULL,
                                         z_3                  decimal(10,5)      NULL,
                                         z_4                  decimal(10,5)      NULL,
                                         z_5                  decimal(10,5)      NULL,
                                         flange               decimal(10,5)      NULL,
                                         neck                 decimal(10,5)      NULL,
                                         prm                  decimal(10,5)      NULL,
                                         motor_load           decimal(10,5)      NULL,
                                         pressure             decimal(10,5)      NULL,
                                         h_1                  decimal(10,5)      NULL,
                                         h_2                  decimal(10,5)      NULL,
                                         h_3                  decimal(10,5)      NULL,
                                         h_4                  decimal(10,5)      NULL,
                                         h_5                  decimal(10,5)      NULL,
                                         theoritical_line_speed decimal(10,5)      NULL,
                                         tip_size             decimal(10,5)      NULL,
                                         tip_type             nvarchar(50)      NULL,
                                         die_size             decimal(10,5)      NULL,
                                         die_type             nvarchar(50)      NULL,
                                         trough_1_temp        decimal(10,5)      NULL,
                                         trough_2_temp        decimal(10,5)      NULL,
                                         spark_tester_ac      decimal(10,5)      NULL,
                                         pay_off_m_load       decimal(10,5)      NULL,
                                         pay_off_m_tension    decimal(10,5)      NULL,
                                         take_up_traverse_gap decimal(10,5)      NULL,
                                         take_up_m_load       decimal(10,5)      NULL,
                                         take_up_tension      decimal(10,5)      NULL,
                                         notes                nvarchar(300)      NULL,
                                         CONSTRAINT pk_sheathing PRIMARY KEY CLUSTERED ( sheathing_data_id  asc )
);
GO

CREATE  TABLE pds.dbo.stranding (
                                    stranding_id         int    IDENTITY  NOT NULL,
                                    stage_description    nvarchar(255)      NOT NULL,
                                    user_id              int      NULL,
                                    machine_id           int      NOT NULL,
                                    stranding_lay_length decimal(10,5)      NULL,
                                    number_of_turns      decimal(10,5)      NULL,
                                    binder_1_lay_length  decimal(10,5)      NULL,
                                    binder_2_lay_length  decimal(10,5)      NULL,
                                    binder_3_lay_length  decimal(10,5)      NULL,
                                    binder_1_tension     decimal(10,5)      NULL,
                                    binder_2_tension     decimal(10,5)      NULL,
                                    binder_3_tension     decimal(10,5)      NULL,
                                    die                  decimal(10,5)      NULL,
                                    forming_die          decimal(10,5)      NULL,
                                    jelly_die_1          decimal(10,5)      NULL,
                                    jelly_die_2          decimal(10,5)      NULL,
                                    jelly_temperature    decimal(10,5)      NULL,
                                    jelly_pressure       decimal(10,5)      NULL,
                                    pay_off_pressure     decimal(10,5)      NULL,
                                    traverse_bitch       decimal(10,5)      NULL,
                                    take_up_pressure     decimal(10,5)      NULL,
                                    notes                nvarchar(300)      NULL,
                                    CONSTRAINT pk_stranding PRIMARY KEY CLUSTERED ( stranding_id  asc )
);
GO

CREATE  TABLE pds.dbo.assembly (
                                   assembly_id          int    IDENTITY  NOT NULL,
                                   stage_description    nvarchar(255)      NOT NULL,
                                   line_speed           float      NULL,
                                   machine_id           int      NOT NULL,
                                   user_id              int      NULL,
                                   traverse_lay         float      NULL,
                                   notes                nvarchar(300)      NULL,
                                   CONSTRAINT pk_assembly PRIMARY KEY CLUSTERED ( assembly_id  asc )
);
GO

CREATE  TABLE pds.dbo.braid (
                                braid_id             int    IDENTITY  NOT NULL,
                                stage_description    nvarchar(255)      NOT NULL,
                                speed                float      NULL,
                                deck_speed           float      NULL,
                                pitch                float      NULL,
                                user_id              int      NULL,
                                machine_id           int      NOT NULL,
                                notes                nvarchar(250)      NULL,
                                CONSTRAINT pk_braid PRIMARY KEY CLUSTERED ( braid_id  asc )
);
GO

CREATE  TABLE pds.dbo.buffering (
                                    buffering_id         int    IDENTITY  NOT NULL,
                                    stage_description    nvarchar(255)      NOT NULL,
                                    machine_id           int      NOT NULL,
                                    user_id              int      NULL,
                                    extruder_1_feeding_1 decimal(10,5)      NULL,
                                    extruder_1_feeding_2 decimal(10,5)      NULL,
                                    extruder_1_z_1       decimal(10,5)      NULL,
                                    extruder_1_z_2       decimal(10,5)      NULL,
                                    extruder_1_z_3       decimal(10,5)      NULL,
                                    extruder_1_prm       decimal(10,5)      NULL,
                                    extruder_1_motor_load decimal(10,5)      NULL,
                                    extruder_1_pressure  decimal(10,5)      NULL,
                                    extruder_2_feeding_1 decimal(10,5)      NULL,
                                    extruder_2_feeding_2 decimal(10,5)      NULL,
                                    extruder_2_z_1       decimal(10,5)      NULL,
                                    extruder_2_z_2       decimal(10,5)      NULL,
                                    extruder_2_z_3       decimal(10,5)      NULL,
                                    extruder_2_prm       decimal(10,5)      NULL,
                                    extruder_2_motor_load decimal(10,5)      NULL,
                                    extruder_2_pressure  decimal(10,5)      NULL,
                                    extruder_1_flange    decimal(10,5)      NULL,
                                    extruder_1_h_1       decimal(10,5)      NULL,
                                    extruder_1_h_2       decimal(10,5)      NULL,
                                    extruder_1_h_3       decimal(10,5)      NULL,
                                    extruder_1_h_4       decimal(10,5)      NULL,
                                    extruder_1_h_5       decimal(10,5)      NULL,
                                    extruder_2_flange    decimal(10,5)      NULL,
                                    extruder_2_h_1       decimal(10,5)      NULL,
                                    extruder_2_h_2       decimal(10,5)      NULL,
                                    extruder_2_h_3       decimal(10,5)      NULL,
                                    extruder_2_h_4       decimal(10,5)      NULL,
                                    extruder_2_h_5       decimal(10,5)      NULL,
                                    theoritical_line_speed decimal(10,5)      NULL,
                                    die                  decimal(10,5)      NULL,
                                    tip                  decimal(10,5)      NULL,
                                    inlet_nipple         decimal(10,5)      NULL,
                                    dry_needle           decimal(10,5)      NULL,
                                    jelly_needle         decimal(10,5)      NULL,
                                    die_insert           decimal(10,5)      NULL,
                                    jelly_die            decimal(10,5)      NULL,
                                    distance_rings       decimal(10,5)      NULL,
                                    spacer_for_die       decimal(10,5)      NULL,
                                    fiber_needle         decimal(10,5)      NULL,
                                    caption_1_m_load     decimal(10,5)      NULL,
                                    caption_2_no_turns   decimal(10,5)      NULL,
                                    caption_2_m_load     decimal(10,5)      NULL,
                                    caption_3_line_tension decimal(10,5)      NULL,
                                    caption_3_m_load     decimal(10,5)      NULL,
                                    trough_1_temp        decimal(10,5)      NULL,
                                    trough_2_temp        decimal(10,5)      NULL,
                                    jelly_prm            decimal(10,5)      NULL,
                                    jelly_pressure       decimal(10,5)      NULL,
                                    jelly_temp           decimal(10,5)      NULL,
                                    take_up_traverse_gap decimal(10,5)      NULL,
                                    take_up_m_load       decimal(10,5)      NULL,
                                    notes                nvarchar(600)      NULL,
                                    CONSTRAINT pk_buffering PRIMARY KEY CLUSTERED ( buffering_id  asc )
);
GO

CREATE  TABLE pds.dbo.coiling (
                                  coiling_id           int    IDENTITY  NOT NULL,
                                  stage_description    nvarchar(255)      NOT NULL,
                                  machine_id           int      NOT NULL,
                                  user_id              int      NULL,
                                  speed                decimal(10,5)      NULL,
                                  notes                nvarchar(250)      NULL,
                                  CONSTRAINT pk_coiling PRIMARY KEY CLUSTERED ( coiling_id  asc )
);
GO

CREATE  TABLE pds.dbo.fo_sheathing (
                                       fo_sheathing_id      int    IDENTITY  NOT NULL,
                                       stage_description    nvarchar(255)      NOT NULL,
                                       user_id              int      NULL,
                                       machine_id           int      NOT NULL,
                                       theoritical_line_speed decimal(10,5)      NULL,
                                       die                  decimal(10,5)      NULL,
                                       tip                  decimal(10,5)      NULL,
                                       pos_no_1             decimal(10,5)      NULL,
                                       pos_no_2             decimal(10,5)      NULL,
                                       pos_no_3             decimal(10,5)      NULL,
                                       pos_no_4             decimal(10,5)      NULL,
                                       pos_no_5             decimal(10,5)      NULL,
                                       pos_no_6             decimal(10,5)      NULL,
                                       pos_no_7             decimal(10,5)      NULL,
                                       pos_no_8             decimal(10,5)      NULL,
                                       pos_no_9             decimal(10,0)      NULL,
                                       pos_no_10            decimal(10,5)      NULL,
                                       pos_no_11            decimal(10,5)      NULL,
                                       pos_no_12            decimal(10,5)      NULL,
                                       pos_no_13            decimal(10,5)      NULL,
                                       pos_no_14            decimal(10,5)      NULL,
                                       pos_no_15            decimal(10,5)      NULL,
                                       pos_no_16            decimal(10,5)      NULL,
                                       pos_no_17            decimal(10,5)      NULL,
                                       pos_no_18            decimal(10,5)      NULL,
                                       pos_no_19            decimal(10,5)      NULL,
                                       pos_no_20            decimal(10,5)      NULL,
                                       pos_no_21            decimal(10,5)      NULL,
                                       pos_no_22            decimal(10,5)      NULL,
                                       pos_no_23            decimal(10,5)      NULL,
                                       pos_no_24            decimal(10,5)      NULL,
                                       pos_no_25            decimal(10,5)      NULL,
                                       pos_no_26            decimal(10,5)      NULL,
                                       pos_no_27            decimal(10,5)      NULL,
                                       pos_no_28            decimal(10,5)      NULL,
                                       pos_no_29            decimal(10,5)      NULL,
                                       pos_no_30            decimal(10,5)      NULL,
                                       pos_no_31            decimal(10,5)      NULL,
                                       pos_no_32            decimal(10,5)      NULL,
                                       pos_no_33            decimal(10,5)      NULL,
                                       pos_no_34            decimal(10,5)      NULL,
                                       pos_no_35            decimal(10,5)      NULL,
                                       pos_no_36            decimal(10,5)      NULL,
                                       pos_no_37            decimal(10,5)      NULL,
                                       pos_no_38            decimal(10,5)      NULL,
                                       pos_no_39            decimal(10,5)      NULL,
                                       pos_no_corrugator    decimal(10,5)      NULL,
                                       caption_line_tension decimal(10,5)      NULL,
                                       caption_m_load       decimal(10,5)      NULL,
                                       trough_1_temp        decimal(10,5)      NULL,
                                       lay_length_yarn_1    decimal(10,5)      NULL,
                                       lay_length_yarn_2    decimal(10,5)      NULL,
                                       tension_left_yarn_1  nvarchar(50)      NULL,
                                       tension_left_yarn_2  nvarchar(50)      NULL,
                                       tension_right_yarn_1 nvarchar(50)      NULL,
                                       tension_right_yarn_2 nvarchar(50)      NULL,
                                       yarn_no_yarn_1       decimal(10,5)      NULL,
                                       yarn_no_yarn_2       decimal(10,5)      NULL,
                                       take_up_dancer_pressure decimal(10,5)      NULL,
                                       take_up_m_load       decimal(10,5)      NULL,
                                       take_up_2_traverse_gap decimal(10,5)      NULL,
                                       take_up_2_m_load     decimal(10,5)      NULL,
                                       notes                nvarchar(300)      NULL,
                                       CONSTRAINT pk_fo_sheathing PRIMARY KEY CLUSTERED ( fo_sheathing_id  asc )
);
GO

CREATE  TABLE pds.dbo.insulation (
                                     insulation_id        int    IDENTITY  NOT NULL,
                                     stage_description    nvarchar(255)      NOT NULL,
                                     user_id              int      NULL,
                                     machine_id           int      NOT NULL,
                                     extruder_1_screw     decimal(10,5)      NULL,
                                     extruder_1_feeding_1 decimal(10,5)      NULL,
                                     extruder_1_feeding_2 decimal(10,5)      NULL,
                                     extruder_1_z_1       decimal(10,5)      NULL,
                                     extruder_1_z_2       decimal(10,5)      NULL,
                                     extruder_1_z_3       decimal(10,5)      NULL,
                                     extruder_1_z_4       decimal(10,5)      NULL,
                                     extruder_1_z_5       decimal(10,5)      NULL,
                                     extruder_1_flange    decimal(10,5)      NULL,
                                     extruder_1_rpm       decimal(10,5)      NULL,
                                     extruder_1_motor_load decimal(10,5)      NULL,
                                     extruder_1_pressure  decimal(10,5)      NULL,
                                     extruder_2_screw     decimal(10,5)      NULL,
                                     extruder_2_feeding_1 decimal(10,5)      NULL,
                                     extruder_2_feeding_2 decimal(10,5)      NULL,
                                     extruder_2_z_1       decimal(10,5)      NULL,
                                     extruder_2_z_2       decimal(10,5)      NULL,
                                     extruder_2_z_3       decimal(10,5)      NULL,
                                     extruder_2_z_4       decimal(10,5)      NULL,
                                     extruder_2_z_5       decimal(10,5)      NULL,
                                     extruder_2_flange    decimal(10,5)      NULL,
                                     extruder_2_rpm       decimal(10,5)      NULL,
                                     extruder_2_motor_load decimal(10,5)      NULL,
                                     extruder_2_pressure  decimal(10,5)      NULL,
                                     extruder_3_screw     decimal(10,5)      NULL,
                                     extruder_3_feeding_1 decimal(10,5)      NULL,
                                     extruder_3_feeding_2 decimal(10,5)      NULL,
                                     extruder_3_z_1       decimal(10,5)      NULL,
                                     extruder_3_z_2       decimal(10,5)      NULL,
                                     extruder_3_z_3       decimal(10,5)      NULL,
                                     extruder_3_z_4       decimal(10,5)      NULL,
                                     extruder_3_z_5       decimal(10,5)      NULL,
                                     extruder_3_flange    decimal(10,5)      NULL,
                                     extruder_3_rpm       decimal(10,5)      NULL,
                                     extruder_3_motor_load decimal(10,5)      NULL,
                                     extruder_3_pressure  decimal(10,5)      NULL,
                                     extruder_1_h_1       decimal(10,5)      NULL,
                                     extruder_1_h_2       decimal(10,5)      NULL,
                                     extruder_1_h_3       decimal(10,5)      NULL,
                                     extruder_1_h_4       decimal(10,5)      NULL,
                                     extruder_1_h_5       decimal(10,5)      NULL,
                                     extruder_2_h_1       decimal(10,5)      NULL,
                                     extruder_2_h_2       decimal(10,5)      NULL,
                                     extruder_2_h_3       decimal(10,5)      NULL,
                                     extruder_2_h_4       decimal(10,5)      NULL,
                                     extruder_2_h_5       decimal(10,5)      NULL,
                                     extruder_3_h_1       decimal(10,5)      NULL,
                                     extruder_3_h_2       decimal(10,5)      NULL,
                                     extruder_3_h_3       decimal(10,5)      NULL,
                                     extruder_3_h_4       decimal(10,5)      NULL,
                                     extruder_3_h_5       decimal(10,5)      NULL,
                                     extruder_4_h_1       decimal(10,5)      NULL,
                                     extruder_4_h_2       decimal(10,5)      NULL,
                                     extruder_4_h_3       decimal(10,5)      NULL,
                                     extruder_4_h_4       decimal(10,5)      NULL,
                                     extruder_4_h_5       decimal(10,5)      NULL,
                                     theoritical_line_speed decimal(10,5)      NULL,
                                     tip_size             decimal(10,5)      NULL,
                                     tip_type             nvarchar(50)      NULL,
                                     die_size             decimal(10,5)      NULL,
                                     die_type             nvarchar(50)      NULL,
                                     trough_1_temp        decimal(10,5)      NULL,
                                     trough_2_temp        decimal(10,5)      NULL,
                                     spark_tester_ac      decimal(10,5)      NULL,
                                     take_up_traverse_gap decimal(10,5)      NULL,
                                     take_up_m_load       decimal(10,5)      NULL,
                                     take_up_dancer_tension decimal(10,5)      NULL,
                                     notes                nvarchar(300)      NULL,
                                     CONSTRAINT pk_insulation PRIMARY KEY CLUSTERED ( insulation_id  asc )
);
GO

ALTER TABLE pds.dbo.assembly ADD CONSTRAINT fk_assembly_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.assembly ADD CONSTRAINT fk_assembly_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.braid ADD CONSTRAINT fk_braid_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.braid ADD CONSTRAINT fk_braid_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.buffering ADD CONSTRAINT fk_buffering_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.buffering ADD CONSTRAINT fk_buffering_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.coiling ADD CONSTRAINT fk_coiling_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.coiling ADD CONSTRAINT fk_coiling_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.fo_sheathing ADD CONSTRAINT fk_fo_sheathing_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.fo_sheathing ADD CONSTRAINT fk_fo_sheathing_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.insulation ADD CONSTRAINT fk_insulation_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.insulation ADD CONSTRAINT fk_insulation_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.machines ADD CONSTRAINT fk_machines_stages FOREIGN KEY ( stage_id ) REFERENCES pds.dbo.stages( stage_id );
GO

ALTER TABLE pds.dbo.premises ADD CONSTRAINT fk_premises_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.premises ADD CONSTRAINT fk_premises_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.rewinding ADD CONSTRAINT fk_rewinding_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.rewinding ADD CONSTRAINT fk_rewinding_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.sections_machines ADD CONSTRAINT fk_sections_machines_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.sections_machines ADD CONSTRAINT fk_sections_machines_sections FOREIGN KEY ( section_id ) REFERENCES pds.dbo.sections( section_id );
GO

ALTER TABLE pds.dbo.sheathing_data ADD CONSTRAINT fk_sheathing_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.sheathing_data ADD CONSTRAINT fk_sheathing_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO

ALTER TABLE pds.dbo.stranding ADD CONSTRAINT fk_stranding_machines FOREIGN KEY ( machine_id ) REFERENCES pds.dbo.machines( machine_id );
GO

ALTER TABLE pds.dbo.stranding ADD CONSTRAINT fk_stranding_users FOREIGN KEY ( user_id ) REFERENCES pds.dbo.users( user_id );
GO
