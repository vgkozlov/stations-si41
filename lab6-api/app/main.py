from fastapi import FastAPI, HTTPException, status
from typing import List
from app.models import PurchaseRequest, PurchaseRequestCreate
from app import storage

app = FastAPI(
    title="API Закупки МТР",
    description="REST API для управления заявками на закупку материально-технических ресурсов",
    version="1.0.0"
)

@app.get("/api/v1/routes/", response_model=List[PurchaseRequest], status_code=status.HTTP_200_OK, tags=["Заявки"])
def get_requests():
    """Получение списка всех заявок"""
    return storage.get_all()

@app.get("/api/v1/routes/{req_id}", response_model=PurchaseRequest, status_code=status.HTTP_200_OK, tags=["Заявки"])
def get_request(req_id: int):
    """Получение детальной информации о заявке по ID"""
    req = storage.get_by_id(req_id)
    if not req:
        raise HTTPException(status_code=404, detail="Заявка не найдена")
    return req

@app.post("/api/v1/routes/", response_model=PurchaseRequest, status_code=status.HTTP_201_CREATED, tags=["Заявки"])
def create_request(item: PurchaseRequestCreate):
    """Создание новой заявки"""
    return storage.create(item)

@app.delete("/api/v1/routes/{req_id}", status_code=status.HTTP_204_NO_CONTENT, tags=["Заявки"])
def delete_request(req_id: int):
    """Удаление заявки по ID"""
    success = storage.delete(req_id)
    if not success:
        raise HTTPException(status_code=404, detail="Заявка не найдена")
    return None