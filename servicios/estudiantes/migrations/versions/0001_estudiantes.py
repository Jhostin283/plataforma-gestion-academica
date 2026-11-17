from alembic import op
import sqlalchemy as sa

revision = "0001"
down_revision = None
branch_labels = None
depends_on = None


def upgrade():
    op.create_table("estudiantes",
        sa.Column("id", sa.Uuid(), primary_key=True),
        sa.Column("documento", sa.String(50), nullable=False),
        sa.Column("nombres", sa.String(150), nullable=False),
        sa.Column("apellidos", sa.String(150), nullable=False),
        sa.Column("correo", sa.String(254), nullable=False),
        sa.Column("estado", sa.String(20), nullable=False),
        sa.UniqueConstraint("documento", name="uq_estudiantes_documento"),
        sa.CheckConstraint("estado IN ('ACTIVO', 'INACTIVO')", name="ck_estudiantes_estado"))


def downgrade():
    op.drop_table("estudiantes")
