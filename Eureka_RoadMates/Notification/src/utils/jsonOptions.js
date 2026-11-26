// expose "id" au lieu de "_id" dans les réponses JSON
module.exports = {
  versionKey: false,
  toJSON: { virtuals: true, transform: (doc, ret) => { delete ret._id; } }
};
