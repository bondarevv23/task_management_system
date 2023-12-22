ALTER TABLE comment ADD created timestamp NOT NULL DEFAULT now();
ALTER TABLE comment ADD modified timestamp NOT NULL DEFAULT now();

ALTER TABLE task ADD created timestamp NOT NULL DEFAULT now();
ALTER TABLE task ADD modified timestamp NOT NULL DEFAULT now();
