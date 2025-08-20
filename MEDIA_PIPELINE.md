# Media Pipeline Implementation

## Overview

The media pipeline provides a clean abstraction for handling product photo uploads with support for local storage in development and a clear path to cloud storage (S3/R2) in production.

## Architecture

### Core Components

1. **MediaStorage Interface** - Abstract contract for media operations
2. **LocalMediaStorage** - Development implementation using local file system
3. **PhotoUploadService** - Business logic for photo uploads with validation
4. **AdminPhotoUploadController** - REST endpoints for photo management
5. **MediaConfig** - Static file serving configuration

### Storage Structure

```
media/
├── 1/                    # Product ID 1
│   ├── photo1_123.jpg   # Timestamped filename
│   └── photo2_456.png
├── 2/                    # Product ID 2
│   └── hero_789.webp
└── ...
```

## Features

### ✅ Implemented

- **File Validation**: JPEG, PNG, WebP formats only
- **Size Limits**: 10MB maximum file size
- **Metadata Extraction**: Automatic width/height detection
- **Duplicate Prevention**: Timestamped filenames
- **Sort Order Management**: Automatic or manual ordering
- **Alt Text Support**: Accessibility-friendly descriptions
- **Local Development**: File-based storage with HTTP serving

### 🔮 Future (S3/R2 Ready)

- **Cloud Storage**: S3/R2 implementation of MediaStorage interface
- **CDN Integration**: CloudFront/Cloudflare support
- **Image Processing**: Thumbnails, resizing, optimization
- **Backup & Replication**: Multi-region storage

## API Endpoints

### Single Photo Upload
```
POST /api/v1/admin/products/{id}/photos/upload
Content-Type: multipart/form-data

Parameters:
- file: Image file (required)
- altText: Alt text (optional)
- sortOrder: Display order (optional)
```

### Multiple Photo Upload
```
POST /api/v1/admin/products/{id}/photos/upload-multiple
Content-Type: multipart/form-data

Parameters:
- files: Array of image files (required)
- altTexts: Array of alt texts (optional)
```

### Response Format
```json
{
  "id": 1,
  "url": "http://localhost:8080/media/1/photo_123.jpg",
  "alt": "Product image",
  "width": 1920,
  "height": 1080,
  "sortOrder": 1,
  "filename": "photo_123.jpg"
}
```

## Configuration

### Development (application-dev.yml)
```yaml
app:
  media:
    base-path: media
    base-url: http://localhost:8080/media
```

### Production (future)
```yaml
app:
  media:
    base-path: s3://bucket-name
    base-url: https://cdn.example.com
```

## File Access

### Public URLs
Photos are served at `/media/{productId}/{filename}` and cached for 1 hour.

### Security
- Admin endpoints require authentication
- Public read access to uploaded files
- No direct file upload to public endpoints

## Testing

### Unit Tests
- `LocalMediaStorageTest` - Storage operations
- `PhotoUploadServiceTest` - Business logic validation

### Integration Tests
- File upload validation
- Database record creation
- Static file serving

## Migration Path to Cloud Storage

1. **Implement S3MediaStorage** class
2. **Update configuration** for production profiles
3. **Add environment variables** for credentials
4. **Deploy with new configuration**
5. **Migrate existing files** (optional)

### Example S3 Implementation
```java
@Service
@Profile("prod")
public class S3MediaStorage implements MediaStorage {
    // S3 client implementation
    // Same interface, different backend
}
```

## Error Handling

### Validation Errors (400)
- Invalid file format
- File too large (>10MB)
- Missing required fields

### Not Found (404)
- Product doesn't exist

### Server Errors (500)
- Storage failures
- Image processing errors

## Performance Considerations

### Development
- Local file I/O
- No network latency
- Suitable for testing

### Production (S3/R2)
- CDN caching
- Parallel uploads
- Async processing
- Image optimization

## Monitoring & Logging

- File upload success/failure
- Storage usage metrics
- Performance metrics
- Error tracking

## Security Best Practices

- File type validation
- Size limits
- Secure file naming
- Access control
- Audit logging
