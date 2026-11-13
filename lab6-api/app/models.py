from pydantic import BaseModel, Field
from datetime import date

class PurchaseRequestCreate(BaseModel):
    title: str = Field(..., description="Предмет закупки", example="Закупка компьютеров")
    department: str = Field(..., description="Подразделение-инициатор", example="ИТ-отдел")
    status: str = Field(default="На согласовании", description="Текущий статус")

class PurchaseRequest(PurchaseRequestCreate):
    id: int = Field(..., description="Уникальный идентификатор")
    created_at: date = Field(..., description="Дата создания")