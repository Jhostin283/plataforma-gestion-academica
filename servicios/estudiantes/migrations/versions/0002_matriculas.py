from alembic import op
import sqlalchemy as sa

revision = '0002'
down_revision = '0001'
branch_labels = None
depends_on = None


def upgrade():
    op.create_table(
        'matriculas',
        sa.Column('id', sa.Uuid(), primary_key=True),
        sa.Column('operacion_id', sa.Uuid(), nullable=False),
        sa.Column('estudiante_id', sa.Uuid(), sa.ForeignKey('estudiantes.id'), nullable=False),
        sa.Column('curso_id', sa.Uuid(), nullable=False),
        sa.Column('estado', sa.String(30), nullable=False),
        sa.Column('codigo_rechazo', sa.Integer(), nullable=True),
        sa.UniqueConstraint('operacion_id', name='uq_matriculas_operacion'),
        sa.CheckConstraint("estado IN ('PENDIENTE', 'ACTIVA', 'FALLIDA', 'COMPENSACION_PENDIENTE', 'CANCELACION_PENDIENTE', 'CANCELADA')", name='ck_matriculas_estado'),
    )
    op.create_index('uq_matriculas_estudiante_curso_vigente', 'matriculas', ['estudiante_id', 'curso_id'], unique=True,
                    postgresql_where=sa.text("estado IN ('PENDIENTE', 'ACTIVA', 'COMPENSACION_PENDIENTE', 'CANCELACION_PENDIENTE')"))


def downgrade():
    op.drop_index('uq_matriculas_estudiante_curso_vigente', table_name='matriculas')
    op.drop_table('matriculas')
