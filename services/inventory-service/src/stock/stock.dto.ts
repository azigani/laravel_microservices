import { IsNumber, IsString, IsOptional, Min } from 'class-validator';

export class CreateStockDto {
    @IsNumber()
    @Min(0)
    productId: number;

    @IsNumber()
    @Min(0)
    quantity: number;

    @IsString()
    @IsOptional()
    location?: string;
}

export class UpdateStockDto {
    @IsNumber()
    @Min(0)
    quantity: number;

    @IsString()
    @IsOptional()
    location?: string;
}

export class AdjustStockDto {
    @IsNumber()
    amount: number; // Valeur positive ou négative
}
