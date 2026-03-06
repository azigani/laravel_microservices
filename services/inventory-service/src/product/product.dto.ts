import {
    IsString,
    IsNumber,
    IsNotEmpty,
    IsOptional,
    MinLength,
    Min,
} from 'class-validator';

export class CreateProductDto {
    @IsString()
    @MinLength(3)
    name: string;

    @IsString()
    @IsNotEmpty()
    sku: string;

    @IsNumber()
    @Min(0)
    price: number;

    @IsString()
    @IsOptional()
    description?: string;

    @IsNumber()
    categoryId: number;
}

export class UpdateProductDto {
    @IsString()
    @IsOptional()
    @MinLength(3)
    name?: string;

    @IsString()
    @IsOptional()
    sku?: string;

    @IsNumber()
    @IsOptional()
    @Min(0)
    price?: number;

    @IsString()
    @IsOptional()
    description?: string;

    @IsNumber()
    @IsOptional()
    categoryId?: number;
}
