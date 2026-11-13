from typing import List, Optional
from datetime import date
from app.models import PurchaseRequest

db: List[PurchaseRequest] = []
current_id = 1

def get_all() -> List[PurchaseRequest]:
    return db

def get_by_id(request_id: int) -> Optional[PurchaseRequest]:
    for req in db:
        if req.id == request_id:
            return req
    return None

def create(item) -> PurchaseRequest:
    global current_id
    new_req = PurchaseRequest(id=current_id, title=item.title, department=item.department, status=item.status, created_at=date.today())
    db.append(new_req)
    current_id += 1
    return new_req

def delete(request_id: int) -> bool:
    global db
    initial_length = len(db)
    db = [req for req in db if req.id != request_id]
    return len(db) < initial_length